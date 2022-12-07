package commit.member.service;

import commit.etc.helper.SessionHelper;
import commit.etc.mail.MailService;
import commit.etc.oauth.KakaoProfile;
import commit.etc.oauth.OAuthToken;
import commit.etc.utils.CommitUtils;
import commit.member.dao.MemberDAO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service("memberService")
public class MemberServiceImpl implements MemberService {

    Logger log = Logger.getLogger(this.getClass());

    @Resource(name = "sessionHelper")
    private SessionHelper sessionHelper;

    @Resource(name = "memberDAO")
    private MemberDAO memberDAO;

    @Autowired//임시비밀번호 메일 전송 관련 서비스
    private MailService mailService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpHeaders httpHeaders;

    @Override
    public int confirmId(Map<String, Object> map) throws Exception {
        int checkId = memberDAO.confirmId(map);
        return checkId;
    }

    @Override
    public int confirmEmail(Map<String, Object> map) throws Exception {
        int checkEmail = memberDAO.confirmEmail(map);
        return checkEmail;
    }

    @Override
    public void join(Map<String, Object> map, HttpServletRequest request, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {

        String loadAddress;
        String addressDetail;
        String fullAddress;

        if (map.get("OAUTH") != null) {
            HttpSession session = request.getSession();
            session.setAttribute("MEM_ID", map.get("MEM_ID"));
            session.setAttribute("MEM_NAME", map.get("MEM_NAME"));//마이페이지에서 -님 이름 뽑아내기 위함.
        }

        String rawPassword = map.get("MEM_PW").toString();
        String password = bCryptPasswordEncoder.encode(rawPassword);
        map.put("MEM_PW", password);

        //주소를 입력했다면(주소는 필수 값이 아님)
        if (!map.get("ZIPCODE").toString().equals("")) {
            //주소 합치는 작업
            loadAddress = (String) map.get("ROAD_ADDRESS");
            //상세주소를 입력하지 않았다면
            if (map.get("ADDRESS_DETAIL") == null) {
                fullAddress = loadAddress + "|";
            } else {
                addressDetail = (String) map.get("ADDRESS_DETAIL");
                fullAddress = loadAddress + "|" + addressDetail;// |이 문자열로 분리해서 DB에 저장하고 꺼내올때도 |를 기준으로 꺼내올거임.
            }
            map.put("ADDRESS", fullAddress);
        }

        //이메일 합치는 작업
        String emailId = (String) map.get("EMAIL_ID");
        String domain = (String) map.get("EMAIL_DOMAIN");
        String email = emailId + "@" + domain;
        map.put("EMAIL", email);

        memberDAO.join(map);
    }

    @Override
    public Map<String, Object> checkMember(Map<String, Object> map, HttpServletRequest request) throws Exception {

        if (map.get("MEM_ID") == null) {//로그인할때는 null이 아님
            map = sessionHelper.make(map, request);
        }
        Map<String, Object> memberInfo = memberDAO.checkMember(map);

        if (memberInfo != null && memberInfo.get("ZIPCODE") != null && !"".equals(memberInfo.get("ZIPCODE"))) {

            //주소 분리해서 map에 담아주기
            String totalAddress = (String) memberInfo.get("ADDRESS");
            int index = totalAddress.indexOf("|");

            String loadAddress = totalAddress.substring(0, index);
            String addressDetail = totalAddress.substring(index + 1);

            memberInfo.put("ROAD_ADDRESS", loadAddress);
            memberInfo.put("ADDRESS_DETAIL", addressDetail);
        }
        return memberInfo;
    }

    @Override
    public String loginCheck(Map<String, Object> map, HttpServletRequest request) throws Exception {

        // 이 기능을 로그인과 회원가입에서도 사용하려고 함
        map = sessionHelper.make(map, request);
        String checkRequest;

        // 이미 로그인 되어있는 상태라면
        if (map.get("MEM_ID") != null) {
            checkRequest = "bad-request";
            return checkRequest;

        } else {
            checkRequest = "correct-request";
            return checkRequest;
        }

    }

    @Override
    public int checkID(Map<String, Object> map) throws Exception {
        return memberDAO.checkID(map);
    }

    @Override
    public String findID(Map<String, Object> map) throws Exception {
        return memberDAO.findID(map);
    }

    @Override
    public int checkPW(Map<String, Object> map) throws Exception {
        return memberDAO.checkPW(map);
    }

    @Override
    public void sendEmail(Map<String, Object> map, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        String tempPW = CommitUtils.getRandomString().substring(0, 10);
        String encodePW = bCryptPasswordEncoder.encode(tempPW);
        map.put("TEMP_PW", encodePW);
        memberDAO.updateTempPW(map);

        String email = map.get("EMAIL").toString();
        String commitEmail = "commitpcShop@gmail.com";
        String title = "[Commit] 🔑 임시비밀번호 보내드립니다.";
        String content = "안녕하세요 Commit입니다.\r\n\r\n발급된 임시비밀번호를 보내드립니다.\r\n\r\n" + tempPW;
        mailService.sendEmail(email, commitEmail, title, content);
    }

    @Override
    public Map<String, Object> kakaoCallback(String code, HttpServletRequest request, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        
        String content_type = "application/x-www-form-urlencoded;charset=utf-8";
        String grant_type = "authorization_code";
        String client_id = "4eaf5def6679143a1fd99e5a3eb4a65e";
        String redirect_uri = "http://deify.iptime.org/member/auth/kakao/callback";
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        String kakaoProfileUrl = "https://kapi.kakao.com/v2/user/me";

        /*-------------------카카오 토큰 받기--------------------*/

        httpHeaders.add("Content-type", content_type);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grant_type);
        params.add("client_id", client_id);
        params.add("redirect_uri", redirect_uri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, httpHeaders);

        ResponseEntity<OAuthToken> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, kakaoTokenRequest, OAuthToken.class);

        String access_token = response.getBody().getAccess_token();

        /*---------------------------카카오 유저 정보 받기---------------------------*/
        //HttpHeader 오브젝트 생성
        httpHeaders.add("Authorization", "Bearer " + access_token);

        //HttpHeader와 HttpBody를 하나의 오브젝트로 담기
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(httpHeaders);

        ResponseEntity<KakaoProfile> kakaoProfile =  restTemplate.exchange(kakaoProfileUrl, HttpMethod.POST, kakaoProfileRequest, KakaoProfile.class);

        /*-------------------헤더 초기화--------------------*/

        httpHeaders.clear();

        //Http 요청하기 - POST 방식으로

        Long kakaoId = kakaoProfile.getBody().getId();
        String kakaoName = kakaoProfile.getBody().getProperties().getNickname();
        String provider = "KAKAO";

        String id = kakaoId + "_" + kakaoName + "_oauth";

        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("MEM_ID", id);
        int result = memberDAO.confirmId(infoMap);
        infoMap.put("result", result);

        if (result == 0) {
            infoMap.put("OAUTH", provider);
            infoMap.put("ID", id);
            infoMap.put("NAME", kakaoName);
            return infoMap;
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("MEM_ID", id);
            session.setAttribute("MEM_NAME", kakaoName);//마이페이지에서 -님 이름 뽑아내기 위함.
        }

        return infoMap;
    }
}
