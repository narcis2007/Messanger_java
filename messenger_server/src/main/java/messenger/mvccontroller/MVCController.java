package messenger.mvccontroller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Logger;

/**
 * Created by Narcis2007 on 11.02.2016.
 */
@Controller
public class MVCController {

    private static final Logger log = Logger.getLogger( MVCController.class.getName() );
        @RequestMapping(value = "/" ,method = RequestMethod.GET)
        public String index() {

//            ModelAndView mav = new ModelAndView("index");
//            mav.addObject("loggedUser", name);
//            return mav;
            return "index";
        }
}
