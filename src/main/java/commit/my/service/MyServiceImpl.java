package commit.my.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import commit.etc.helper.Alert;
import commit.etc.helper.SessionHelper;
import commit.my.dao.MyDAO;

@Service("myService")
public class MyServiceImpl implements MyService {

	Logger log = Logger.getLogger(this.getClass());

	
	@Resource(name="sessionHelper")
	private SessionHelper sessionHelper;
	
	@Resource(name = "alert")
	private Alert alert;
	
	@Resource(name = "myDAO")
	private MyDAO myDAO;

	@Override
	public void updateMyInfo(Map<String, Object> map, HttpServletRequest request, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {

		String inputPassword = map.get("MEM_PW").toString();
		String encodePassword = bCryptPasswordEncoder.encode(inputPassword);
		map.put("MEM_PW", encodePassword);

		map = sessionHelper.make(map,request);

		String loadAddress;
		String addressDetail;
		String fullAddress;

		//주소를 입력했다면(주소는 필수 값이 아님)
		if(!"".equals(map.get("ZIPCODE").toString())) {
			//주소 합치는 작업
			loadAddress = (String) map.get("ROAD_ADDRESS");
			//상세주소를 입력하지 않았다면
			if("".equals(map.get("ADDRESS_DETAIL"))){
				fullAddress = loadAddress + "|";
			} else {
				addressDetail = (String) map.get("ADDRESS_DETAIL");
				fullAddress = loadAddress + "|" + addressDetail;// |이 문자열로 분리해서 DB에 저장하고 꺼내올때도 |를 기준으로 꺼내올거임.
			}
			map.put("ADDRESS", fullAddress);
		}

		myDAO.updateMyInfo(map);
	}

	@Override
	public void deleteMyInfo(Map<String, Object> map, HttpServletRequest request) throws Exception {
		map = sessionHelper.make(map, request);

		myDAO.deleteMyInfo(map);
	
		
		sessionHelper.remove(request);
		

	}

	@Override//admin 페이지의 회원목록의 회원상세에 회원이 주문한 목록 안보기로함
	public List<Map<String, Object>> myOrderList(Map<String, Object> map, HttpServletRequest request) throws Exception {

		map = sessionHelper.make(map, request);

		return myDAO.myOrderList(map);
	}

	@Override
	public List<Map<String, Object>> myQnaList(Map<String, Object> map, HttpServletRequest request) throws Exception {

		map = sessionHelper.make(map, request);

		return myDAO.myQnaList(map);
	}

	@Override
	public Map<String, Object> myQnaDetail(Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		map = sessionHelper.make(map, request);
		Map<String, Object> myQnaDetail = myDAO.myQnaDetail(map);
		try {
			if (!myQnaDetail.get("MEM_ID").equals(map.get("MEM_ID")) && map.get("admin") == null) {
				throw new Exception();
			}
		} catch (Exception e) {
			
			alert.make(response, "잘못된 접근입니다.", "/main");
			
		}

		return myQnaDetail;
	}

	@Override
	public List<Map<String, Object>> myCouponList(Map<String, Object> map, HttpServletRequest request)
			throws Exception {

		map = sessionHelper.make(map, request);

		return myDAO.myCouponList(map);
	}

	@Override
	public Map<String, Object> orderDetail(Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		map = sessionHelper.make(map, request);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 얘는 하나의 주문번호에 대한 여러상품 정보를 가져와야하니 list로
		List<Map<String, Object>> order = myDAO.selectFromOrder(map);
		// 얘는 하나의 주문번호에 대한 주문정보 하나만 가져오면 되니 하나의 map으로
		Map<String, Object> orderInfo = myDAO.selectFromOrderInfo(map);

		try {
			if (!orderInfo.get("MEM_ID").equals(map.get("MEM_ID")) && map.get("admin") == null) {
				throw new Exception();
			}
		} catch (Exception e) {
			
			
			alert.make(response, "잘못된 접근입니다.", "/main");
		}

		resultMap.put("order", order);
		resultMap.put("orderInfo", orderInfo);

		return resultMap;

	}

}