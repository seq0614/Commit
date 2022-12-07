package commit.etc.helper;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
@Component("sessionHelper")
public class SessionHelper {

	public Map<String, Object> make(Map<String, Object> map, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		String MEM_ID = (String) session.getAttribute("MEM_ID");
		String admin = (String) session.getAttribute("admin");
		
	
		if(MEM_ID == null) {//로그인 안했을때(근데 이거 어차피 인터셉터에서 확인해줄거라 굳이 필요없는데 2중 확인!
			return map;
		}
		else {
			map.put("MEM_ID", MEM_ID);
				if(admin != null) {
					map.put("admin", admin);
				}
				
				return map;
		}
		
	}
	
	
	public void remove(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		session.removeAttribute("MEM_ID");
		session.removeAttribute("admin");
	}

}
