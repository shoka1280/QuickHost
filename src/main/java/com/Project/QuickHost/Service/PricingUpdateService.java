package com.Project.QuickHost.Service;

import com.Project.QuickHost.Entity.Hotel;
import com.Project.QuickHost.Entity.HotelMinPrice;
import com.Project.QuickHost.Entity.Inventory;
import com.Project.QuickHost.Repository.HotelMinPriceRepo;
import com.Project.QuickHost.Repository.HotelRepo;
import com.Project.QuickHost.Repository.InventoryRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//Update the prices
@Service
@RequiredArgsConstructor

@Slf4j
@Transactional

public class PricingUpdateService {
    //Adding scheduler update the inventory and HotelMinPrice tables every hour
    private final HotelRepo hotelRepo;
    private final HotelMinPriceRepo hotelMinPriceRepo;
    private final InventoryRepo inventoryRepo;
    private final PricingService priceService;
    //creating scheduler to update prices every hour
    @Scheduled(cron = "0 0 */5 * * *") // every 5 hours


    public void UpdatePrices()
    {
        int page=0;//which page you want to start
        int batchsize=100;//number of items per page
        while(true)
        {
            Page<Hotel> hotelPage=hotelRepo.findAll(PageRequest.of(page,batchsize));
                    if(hotelPage.isEmpty())
                    {
                        break;
                    }
                    hotelPage.getContent().forEach(hotel->updateHotelPrice(hotel));//for each hotel updateHotelPrice is called
                    page++;

        }

    }
    public void updateHotelPrice(Hotel hotel)
    {
        log.info("Updating prices for hotel: {}",hotel.getId());
        LocalDate startDate=LocalDate.now();
        LocalDate endDate=LocalDate.now().plusYears(1);


        List<Inventory> inventoryList=inventoryRepo.findByHotelAndDateBetween(hotel,startDate,endDate);
        //updating inventory by dyanamic prices
        updateInventoryPrices(inventoryList);
       //updating hotel min prices based on inventory[we will since minimum room price per hotel ]
        updateHotelMinPrice(hotel,inventoryList,startDate ,endDate);


    }

    private void updateHotelMinPrice(Hotel hotel, List<Inventory> inventoryList, LocalDate startDate, LocalDate endDate) {
        Map<LocalDate,BigDecimal> dailyMinPrice=inventoryList.stream()
                .collect(Collectors.groupingBy(Inventory::getDate//grouping by date
                ,Collectors.mapping(Inventory::getPrice,Collectors.minBy(Comparator.naturalOrder()))//extract price and find minimum price for that data
                ))
                .entrySet().stream()//maps you get convert its entryset into stream for futher
                .collect(Collectors.toMap(Map.Entry::getKey,e->e.getValue().orElse(BigDecimal.ZERO)));//take date as keu and unwrap the price

        //PreParing hotelMinPrice entity in bulk
//        rom a list of Inventory objects, you want a map:
//        date → minimum price on that date.
        List<HotelMinPrice> hotelPrices=new ArrayList<>();
        dailyMinPrice.forEach((date, price) -> {
            HotelMinPrice hotelPrice=hotelMinPriceRepo.findByHotelAndDate(hotel,date)
                    .orElse(new HotelMinPrice(hotel,date));
            //setting price of indivisual hotel
            hotelPrice.setPrice(price);
            //adding price to list so that we add futher add to db with less ques
            hotelPrices.add(hotelPrice);

        });
        hotelMinPriceRepo.saveAll(hotelPrices);//batch insert or update
    }


    private void updateInventoryPrices(List<Inventory>inventoryList)
    {
        inventoryList.forEach(inventory->
        {
            //calculating dyanamic price
            BigDecimal dynamicPrice=priceService.calculateDynamicPricing(inventory);
            //updating the price
            inventory.setPrice(dynamicPrice);
             

        });
        inventoryRepo.saveAll(inventoryList);//batch update

    }




}
