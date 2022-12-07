package commit.qna.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import commit.etc.dao.AbstractDAO;
@Repository("qnaDAO")
public class QnaDAO extends AbstractDAO{

	public void insertAnswer(Map<String, Object> map) {
		insert("qna.insertAdminQna", map);
		
	}

	public void insertQuestion(Map<String, Object> map) {
		insert("qna.insertMemQna", map);
		
	}

	public void updateState(Map<String, Object> map) {
		update("qna.updateState",map);
		
	}

	@SuppressWarnings("unchecked")
	public Map<String,Object> detailQna(Map<String, Object> map) {
		
		return (Map<String, Object>) selectOne("qna.detailQna", map);
		
	}

	public void updateQna(Map<String, Object> map) {
		update("qna.updateQna", map);
		
	}

	public void deleteQna(Map<String, Object> map) {
		update("qna.deleteQna", map);
		
	}

}
