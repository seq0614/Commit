package commit.product.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import commit.basket.dao.BasketDAO;
import commit.etc.helper.SessionHelper;
import commit.product.dao.ProductDAO;

@Service("productService")
public class ProductServiceImpl implements ProductService {

	Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "productDAO")
	private ProductDAO productDAO;

	@Resource(name = "basketDAO")
	private BasketDAO basketDAO;

	@Resource(name = "sessionHelper")
	private SessionHelper sessionHelper;

	@Override
	public List<Map<String, Object>> getProList(Map<String, Object> map) throws Exception {

		return productDAO.getProList(map);
	}

	@Override
	public Map<String, Object> selectProDetail(Map<String, Object> map) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> detail = productDAO.selectProDetail(map);
		List<Map<String, Object>> image = productDAO.selectProImage(map);
		List<Map<String, Object>> review = productDAO.selectProReview(map);
		List<Map<String, Object>> qna = productDAO.selectProQna(map);
		resultMap.put("detail", detail);
		resultMap.put("image", image);
		resultMap.put("review", review);
		resultMap.put("qna", qna);
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> getProInfo(String[] PRO_IDX, String[] AMOUNT) {

		List<Map<String, Object>> proInfoList = new ArrayList<Map<String, Object>>();
		Map<String, Object> proInfo;
		Map<String, Object> orderProduct = new HashMap<String, Object>();
		int orderAmount;
		int stockAmount;
	
		for (int i = 0; i < PRO_IDX.length; i++) {
			
			orderProduct.put("PRO_IDX", PRO_IDX[i]);
			orderProduct.put("AMOUNT", AMOUNT[i]);
			proInfo = productDAO.getProInfo(orderProduct);
			orderAmount = Integer.parseInt(orderProduct.get("AMOUNT").toString());// 주문수량
			stockAmount = Integer.parseInt(proInfo.get("STOCK").toString());// 상품재고
			// 상품 재고보다 주문 수량이 많을때
			if (orderAmount > stockAmount) {
				return new ArrayList<>();
			}
			proInfo.put("AMOUNT", AMOUNT[i]);
			proInfoList.add(proInfo);

		}
		
		return proInfoList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void insertOrder(Map<String, Object> map, HttpServletRequest request)
			throws Exception {

		//주문정보
		Map<String,Object> orderInfoMap = (Map<String, Object>) map.get("info");
		orderInfoMap = sessionHelper.make(orderInfoMap, request);// 회원아이디 얻어옴(session에서)
		productDAO.insertOrderInfo(orderInfoMap);
		// 주문 insert 할때 반복문 돌리기!!
		if (orderInfoMap.get("CP_IDX") != null) {
			productDAO.updateMemberCoupon(orderInfoMap);
		}
		
		//주문상품정보
		List<Map<String,Object>> orderPro = (List<Map<String, Object>>) map.get("pro");
		

		for (Map<String, Object> order : orderPro) {
			// order에 상품번호와 주문 수량이 담겨있음
			order.put("ORDER_IDX", orderInfoMap.get("ORDER_IDX"));
			productDAO.insertProOrder(order);
			// order.put("order", "insertOrder");//상품 재고를 update시키기위한 구별값 order라는 값이 있으면 재고
			// - 없으면(취소일때) +
			productDAO.updateProStock(order);// 사용자가 주문한 상품 번호에 주문한 수량만큼 product 테이블 amount(재고) 수정 + 주문횟수 1 증가

			// 내 장바구니에 내가 담아둔 상품이 있는지 확인
			order.put("MEM_ID", orderInfoMap.get("MEM_ID"));// 장바구니에서 사용해야 됨
			int check = basketDAO.checkBasket(order);
			if (check != 0) {
				basketDAO.deleteBasket(order);
			}
		}
	}

	@Override
	public int cancelOrder(Map<String, Object> map, HttpServletRequest request) {

		map = sessionHelper.make(map, request);
		Map<String, Object> InfoMap = productDAO.checkOrderState(map);

		//배송전 일때만 주문 취소 가능
		if (InfoMap.get("STATE").equals("B") && InfoMap.get("MEM_ID").equals(map.get("MEM_ID"))) {
			productDAO.cancelOrder(map);
			return 1;
		}

		return -1;
	}

}
