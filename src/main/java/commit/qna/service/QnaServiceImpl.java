package commit.qna.service;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import commit.etc.helper.SessionHelper;
import commit.qna.dao.QnaDAO;

@Service("qnaService")
public class QnaServiceImpl implements QnaService{

	Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name="sessionHelper")
	private SessionHelper sessionHelper;
	
	@Resource(name="qnaDAO")
	private QnaDAO qnaDAO;

	@Override
	public Map<String,Object> detailQna(Map<String, Object> map) throws Exception {
		
		return qnaDAO.detailQna(map);
		
	}

	@Override
	public void updateQna(Map<String, Object> map) throws Exception {
		qnaDAO.updateQna(map);
		
	}

	@Override
	public void deleteQna(Map<String, Object> map, HttpServletRequest request) throws Exception {
		
		map = sessionHelper.make(map, request);
		
		qnaDAO.deleteQna(map);
		
		
	}

	@Override
	public void insertQna(Map<String, Object> map, HttpServletRequest request) {
		map = sessionHelper.make(map, request);
		
		//admin이 답변 다는거
		if(map.get("admin")!= null) {
			qnaDAO.insertAnswer(map);
			qnaDAO.updateState(map);//루트 글 답변 상태도 업데이트 시킴
			
			
		}else {//회원이 답변다는거
			qnaDAO.insertQuestion(map);
		}
		
	}

}
