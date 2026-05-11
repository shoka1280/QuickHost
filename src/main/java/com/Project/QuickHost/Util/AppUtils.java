package com.Project.QuickHost.Util;

import com.Project.QuickHost.Entity.User;
import com.Project.QuickHost.exception.UnAuthorisedException;
import org.springframework.security.core.context.SecurityContextHolder;

public class AppUtils {
    public static User getCurrentUser()
    {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
