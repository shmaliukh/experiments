package com.vshmaliukh.test_project;

import com.vshmaliukh.test_project.entities.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@Slf4j
@Controller
@AllArgsConstructor
public class MyController {

    final UserService userService;

    @GetMapping("/hello")
    public ModelAndView doGetHello(@RequestParam(defaultValue = "") String action,
                                   Principal principal,
                                   ModelMap modelMap) {
        User user = userService.userRepository.findByEmail(principal.getName());

        modelMap.addAttribute(user);
        modelMap.addAttribute("action", action);
        return new ModelAndView("hello", modelMap);
    }

    @PreAuthorize("hasAnyAuthority('WRITE_PRIVILEGE')")
    @PostMapping("/admin-action")
    public ModelAndView doGetAdminAction(ModelMap modelMap) {

        modelMap.addAttribute("action", "admin-action");
        return new ModelAndView("redirect:/hello", modelMap);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public void handleError(HttpServletResponse response) throws IOException {
        log.warn("access denied // response: '{}'", response.getStatus());
        response.sendRedirect("/login");
    }


}
