package com.workshop.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import com.workshop.Service.UserDetailServiceImpl;
import com.workshop.Service.UserService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	
	private  UserService userService;

    public SecurityConfig(@Lazy UserService userService) {
        this.userService = userService;
    }
    
//    public UserService userService() {
//    	return new UserDetailServiceImpl();
//    }
   
	   
	      @Bean
	    public BCryptPasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	    
	    @Bean
		@Primary
		AuthenticationManagerBuilder  authenticationManager(AuthenticationManagerBuilder auth) throws Exception {
			
	    	auth.authenticationProvider(authenticationProvider());
	    
		    return auth;
		}
	    
	    @Bean
	    public DaoAuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
	        auth.setUserDetailsService( userService);
	        auth.setPasswordEncoder(passwordEncoder());
	        return auth;
	    }
	    
	    
	    @Bean
		 public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
	    	
	    	http
            .authorizeHttpRequests()
//                .requestMatchers("/","/resources/**", "/registration", "/","//","/change-pass","/fonts/**","/register","/css/**","/js/**","/images/**","/cab","/invoice","/getUserBooking","/thankyou","/testpage","/book","/contact","/services","/pune","/error","/Pune","/Mumbai","/Dapoli","/Alibag","Trimbkeshwar","/Lgatpuri","/Une","Bangalore","/Mahabaleshwar", "/Nashik","/Satara","/Kolhapur","/Shirdi","/Ahmednagar", "/Sangamner","/Ratnagiri","Jalana","/Goa","/Delhi","/Surat","/Corporate-Cab-Services-Indore","/Lonavala","/Bhimashankar","/Lgatpuri","/Panchagani","/Corporate","/Corporate1","/Corporate2","/Corporate3","/Telangana","/Corporate5","/Corporate6","/Corporate7","/Airport","/Airport1","/Airport2","/Airport3","/Airport4","/Airport5","/Airport6","/Airport7", "Corporate","/Corporate Cabs in Mumbai","/Corporate Cabs in Indor","/Corporate Cabs in Surat","/Airport Cabs in Delhi","/Airport Cabs in Mumbai","/Airport Cabs in Bangalore","/Airport Cabs in Pune","/Airport Cabs in Hyderabad","/Airport Cabs in Chennai","/Airport Cabs in Indor","/Airport Cabs in Surat").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/user/**").hasAuthority("USER")
                .requestMatchers("/vendor/**").hasAuthority("VENDOR")
                .requestMatchers("/**").permitAll()

                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .successHandler(roleBasedAuthenticationSuccessHandler())
                .defaultSuccessUrl("/")

                .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")

                .permitAll()
                .and()
                .csrf().disable();
	    	
			return http.build();

	    }
	    
	    @Bean
	    public RoleBasedAuthenticationSuccessHandler roleBasedAuthenticationSuccessHandler() {
	        RoleBasedAuthenticationSuccessHandler successHandler = new RoleBasedAuthenticationSuccessHandler();
	        successHandler.setAdminTargetUrl("/admin/dashboard");
	        successHandler.setDoctorTargetUrl("/doctor/dashboard");
             successHandler.setUserTargetUrl("/user/dashboard");
	        return successHandler;
	    }
	 // @Autowired
		   // private PasswordEncoder passwordEncoder;


		    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
