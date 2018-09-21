package data.platform.authority.security;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserDetailsImpService extends UserDetailsService{
	public UserDetails loadUserByUserbua(Map<String,String> userMap);

}
