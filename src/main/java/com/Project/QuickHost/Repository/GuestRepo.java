package com.Project.QuickHost.Repository;

import com.Project.QuickHost.Entity.Guest;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepo extends JpaRepository<Guest,Long> {
}
