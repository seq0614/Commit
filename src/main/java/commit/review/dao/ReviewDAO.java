package commit.review.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import commit.etc.dao.AbstractDAO;

@Repository("reviewDAO")
public class ReviewDAO extends AbstractDAO {

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectMyReview(Map<String, Object> map) {
		
		return selectList("review.selectMyReview", map);
	}

	public void insertReview(Map<String, Object> map) {
		insert("review.insertReview", map);
		
	}

	public void deleteReview(Map<String, Object> map) {
		update("review.deleteReview", map);
		
	}

	public int checkValid(Map<String, Object> map) {
		
		return (int) selectOne("review.checkValid", map);
	}

	public int checkOverlap(Map<String, Object> map) {
		
		return  (int) selectOne("review.checkOverlap", map);
	}
	

}
