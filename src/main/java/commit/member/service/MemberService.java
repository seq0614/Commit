package commit.member.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface MemberService {

	Map<String, Object> checkMember(Map<String, Object> map, HttpServletRequest request) throws Exception;

	int confirmId(Map<String, Object> map) throws Exception;

	int confirmEmail(Map<String, Object> map) throws Exception;

	void join(Map<String, Object> map, HttpServletRequest request, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception;

	String loginCheck(Map<String, Object> map, HttpServletRequest request) throws Exception;

	int checkID(Map<String, Object> map) throws Exception;

	String findID(Map<String, Object> map) throws Exception;

	int checkPW(Map<String, Object> map) throws Exception;

	void sendEmail(Map<String, Object> map, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception;

	Map<String, Object> kakaoCallback(String code,  HttpServletRequest request, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception;

}
