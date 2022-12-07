package commit.review.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import commit.etc.helper.SessionHelper;
import commit.review.dao.ReviewDAO;

@Service("reviewSerive")
public class ReviewServiceImpl implements ReviewService {

	Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "sessionHelper")
	private SessionHelper sessionHelper;

	@Resource(name = "reviewDAO")
	private ReviewDAO reviewDAO;

	@Override
	public List<Map<String, Object>> selectMyReview(Map<String, Object> map, HttpServletRequest request)
			throws Exception {

		map = sessionHelper.make(map, request);

		return reviewDAO.selectMyReview(map);
	}

	@Override
	public void insertReview(Map<String, Object> map, HttpServletRequest request) throws Exception {
		map = sessionHelper.make(map, request);

		reviewDAO.insertReview(map);

	}

	@Override
	public void deleteReview(Map<String, Object> map) throws Exception {

		reviewDAO.deleteReview(map);
	}

	@Override
	public String checkValid(Map<String, Object> map, HttpServletRequest request) throws Exception {

		String path;

		map = sessionHelper.make(map, request);
		// 조건 하나 더 체크 사용자가 이미 리뷰를 남기지는 않았는지.. -> 같은 주문 번호로 같은 상품의 리뷰를 남기지 않았는지
		int overlap = reviewDAO.checkOverlap(map);

		if (overlap == 0) {// 리뷰를 남기지 않을때 우선순위 1
			
			int check = reviewDAO.checkValid(map);
			if (check == 1) {// 리뷰 작성 가능
				
				path = "/review/writeForm";
				return path;
			} else {// 유효기간이 지났거나 주문을 하지않고 리뷰를 작성하려고 시도할때(주소쳐서)

				path = "/alert/invalidReview";
				
				return path;
			}
		}

		//System.out.println("리뷰 중복!!!!!!!");
		path = "/alert/overlapReview";
		return path;

	}

}
