package es.uca.cm.admin.webUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class WebUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final WebUserRepository userRepository;

    public WebUserService(WebUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String Cifrar (String s){
        return passwordEncoder.encode(s);
    }

    public WebUser save(WebUser user){
        if(user.getId() == null) user.setPass(passwordEncoder.encode(user.getPass()));
        userRepository.save(user);
        return user;
    }

    public long count(){
        return userRepository.count();
    }

    public WebUser changePass(WebUser user, String pass){
        user.setPass(passwordEncoder.encode(pass));
        userRepository.save(user);
        return user;
    }
}
