package commit.admin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import commit.admin.dao.AdminDAO;
import commit.coupon.dao.CouponDAO;
import commit.etc.etc.Paging;
import commit.etc.helper.Alert;
import commit.etc.utils.ImageUtils;
import commit.product.dao.ProductDAO;

@Service("adminService")
public class AdminServiceImpl implements AdminService {

	Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "adminDAO")
	private AdminDAO adminDAO;

	@Resource(name = "productDAO")
	private ProductDAO productDAO;

	@Resource(name = "imageUtils")
	private ImageUtils imageUtils;

	@Resource(name = "paging")
	private Paging paging;

	@Resource(name = "alert")
	private Alert alert;

	@Override
	public List<Map<String, Object>> getOrderList(Map<String, Object> map) throws Exception {
		
		map.put("totalRecord", adminDAO.countOrderRecord(map));
		Map<String, Object> resultMap = paging.calc(map,10);
		List<Map<String, Object>> list = adminDAO.getOrderList(resultMap);
		list.add(resultMap);
		return list;
	}

	@Override
	public void updateOrder(Map<String, Object> map) throws Exception {
		// map 안에는 주문 번호랑 state가 담겨있고 만약 state가 환불 완료(F)라면
		List<Map<String, Object>> list;

		adminDAO.updateOrder(map);// 주문번호에 대한 상태는 변경시켜주고

		// 만약 주문 상태가 F 환불완료라면 주문한 수량만큼 상품재고를 다시 +, 쿠폰사용했으면 다시 원래대로
		if (map.get("STATE").equals("F")) {

			// pro_order 테이블로부터 정보를 꺼내옴
			list = adminDAO.getProInfo(map);
			for (Map<String, Object> order : list) {
				order.put("cancel", "orderCancel");
				productDAO.updateProStock(order);
			}

			Map<String, Object> check = adminDAO.checkCoupon(map);// order_info 테이블로부터 정보를 꺼내옴
			if (check.get("CP_IDX") != null) {
				check.put("cancel", "orderCancel");
				productDAO.updateMemberCoupon(check);
			}

		}
	}

	@Override
	public List<Map<String, Object>> getOrderDetail(Map<String, Object> map) throws Exception {

		return adminDAO.getOrderDetail(map);
	}

	@Override
	public List<Map<String, Object>> selectMemberList(Map<String, Object> map) throws Exception {

		map.put("totalRecord", adminDAO.countMemberRecord(map));
		Map<String, Object> resultMap = paging.calc(map,10);
		List<Map<String, Object>> list = adminDAO.selectMemberList(resultMap);
		list.add(resultMap);
		return list;
	}

	@Override
	public Map<String, Object> selectMemberDetail(Map<String, Object> map) throws Exception {

		Map<String, Object> temp = adminDAO.selectMemberDetail(map);

		if (temp != null) {
			String address = (String) temp.get("ADDRESS");
			int division = address.indexOf("|");
			if (division != -1) {
				temp.put("ADDRESS1", address.substring(0, division));
				temp.put("ADDRESS2", address.substring(division + 1));
			}
		}

		return temp;
	}

	@Override
	public void deleteMember(Map<String, Object> map) throws Exception {
		adminDAO.deleteMember(map);
	}

	@Override
	public List<Map<String, Object>> selectProList(Map<String, Object> map) throws Exception {
		
		map.put("totalRecord", adminDAO.countProRecord(map));
		Map<String, Object> resultMap = paging.calc(map,10);
		List<Map<String, Object>> list = adminDAO.selecProList(resultMap);
		list.add(resultMap);
		return list;
	}

	@Override
	public void insertPro(Map<String, Object> map, HttpServletRequest request) throws Exception {

		Map<String, Object> proInfoMap = imageUtils.parseInsertMainImg(map, request);
		adminDAO.insertPro(proInfoMap);// 상품 등록 + 메인 사진

		if (map.get("PRO_GROUP").equals("STUFF")) {
			adminDAO.insertDetail(proInfoMap);
		}

		List<Map<String, Object>> imageList = imageUtils.parseinsertSubImage(map, request);
		for (Map<String, Object> imageMap : imageList) {
			adminDAO.insertProImg(imageMap);
		}

	}

	@Override
	public Map<String, Object> detailPro(Map<String, Object> map) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> proInfoMap = adminDAO.detailPro(map);
	 	if("STUFF".equals(proInfoMap.get("PRO_GROUP"))) {
	 		Map<String, Object> detailStuff = adminDAO.detailStuff(map);
	 		resultMap.put("stuffDetail", detailStuff);
	 	}
		List<Map<String, Object>> ImageList = adminDAO.detailProImg(map);


		resultMap.put("proInfo", proInfoMap);
		resultMap.put("proImage", ImageList);

		return resultMap;
	}

	@Override
	public void updatePro(Map<String, Object> map, HttpServletRequest request) throws Exception {

		Map<String, Object> imageMap = imageUtils.parseInsertMainImg(map, request);

		// 메인 이미지는 필수이기 때문에 삭제 처리 필요없음(새로 업데이트한 메인 사진 파일이 없다면 기존에 있는 파일을 사용하면 된다.
		// 상품정보 변경
		adminDAO.updatePro(imageMap);
		
		if("STUFF".equals(imageMap.get("PRO_GROUP"))) {
			adminDAO.updateProDetail(imageMap);
		}

		// 기존에 있었던 이미지 삭제 처리
		adminDAO.deleteImgList(map);

		List<Map<String, Object>> list = imageUtils.parseUpdateSubImage(map, request);

		for (Map<String, Object> tempMap : list) {
			if (tempMap.get("NEW_FILE").equals("Y")) {
				adminDAO.insertProImg(tempMap);
			} else {
				adminDAO.updateImgList(tempMap);
			}
		}

	}

	@Override
	public void deletePro(Map<String, Object> map, HttpServletResponse response) throws Exception {
		adminDAO.deletePro(map);
		adminDAO.deleteImageList(map);

		alert.make(response, "삭제되었습니다.", "/admin/pro/list");
	}
}
