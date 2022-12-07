package commit.notice.service;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import commit.etc.etc.Paging;
import commit.etc.helper.SessionHelper;
import commit.etc.utils.FileUtils;
import commit.notice.dao.NoticeDAO;

@Service("noticeService")
public class NoticeServiceImpl implements NoticeService{

	Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name = "noticeDAO")
	private NoticeDAO noticeDAO;
	
	@Resource(name="sessionHelper")
	private SessionHelper sessionHelper;
	
	@Resource(name="fileUtils")
	private FileUtils fileUtils;
	
	@Resource(name="paging")
	private Paging paging;
	
	@Override
	public List<Map<String, Object>> selectNoticeList(Map<String, Object> map) throws Exception {
		
		map.put("totalRecord", noticeDAO.countRecord(map));
		
		Map<String, Object> resultMap = paging.calc(map,10);
		List<Map<String, Object>> list = noticeDAO.selectNoticeList(resultMap);
		list.add(resultMap);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> selectCommentData(Map<String, Object> map) throws Exception {
		
		map.put("totalRecord", noticeDAO.countCommentRecord(map));
		Map<String, Object> resultMap = paging.calc(map,10);
		List<Map<String, Object>> list = noticeDAO.selectNoticeComments(resultMap);
		list.add(resultMap);
		return list;
	}
	

	@Override
	public void insertNotice(Map<String, Object> map, HttpServletRequest request) throws Exception {
		noticeDAO.insertNotice(map);
		
		List<Map<String,Object>> list = fileUtils.parseInsertFileInfo(map, request);
		for(int i=0, size=list.size(); i<size; i++){
			noticeDAO.insertFile(list.get(i));
		}
	}

	
	
	@Override
	public Map<String, Object> selectNoticeDetail(Map<String, Object> map, HttpServletRequest request) throws Exception {
		map = sessionHelper.make(map, request);
		if(map.get("admin") == null) {
			noticeDAO.updateHitCount(map);			
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> detail = noticeDAO.selectNoticeDetail(map);
		List<Map<String,Object>> list = noticeDAO.selectFileList(map);
		
		if(detail != null) {
			resultMap.put("list", list);
			resultMap.put("detail", detail);
		}
		return resultMap;
	}
	/*@Override
	public Map<String, Object> selectNoticeUpdate(Map<String, Object> map) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> detail = noticeDAO.selectNoticeDetail(map);
		//List<Map<String,Object>> comments = noticeDAO.selectNoticeComments(map);
		List<Map<String,Object>> list = noticeDAO.selectFileList(map);
		resultMap.put("list", list);
		resultMap.put("detail", detail);
		return resultMap;
	}*/

	@Override
	public void updateNotice(Map<String, Object> map, HttpServletRequest request) throws Exception {
		noticeDAO.updateNotice(map);
		
		noticeDAO.deleteFileList(map);
		List<Map<String,Object>> list = fileUtils.parseUpdateFileInfo(map, request);
		Map<String,Object> tempMap = null;
		for(int i=0, size=list.size(); i<size; i++){
			tempMap = list.get(i);
			if(tempMap.get("IS_NEW").equals("Y")){
				noticeDAO.insertFile(tempMap);
			}
			else{
				noticeDAO.updateFile(tempMap);
			}
		}
	}

	
	@Override
	public void deleteNotice(Map<String, Object> map) throws Exception {
		noticeDAO.deleteNotice(map);
		
	}
	
	
	@Override
	public void selectFileInfo(Map<String, Object> commitMap, HttpServletResponse response, HttpServletRequest request) throws Exception {
		Map<String, Object> map = noticeDAO.selectFileInfo(commitMap);
		String storedFileName = (String)map.get("STORED_NAME");
		String originalFileName = (String)map.get("ORIGINAL_NAME");
		String filePath = request.getSession().getServletContext().getRealPath("/") + "static/uploadFile/";
		byte fileByte[] = org.apache.commons.io.FileUtils.readFileToByteArray(new File(filePath+storedFileName));
			
		response.setContentType("application/octet-stream");
		response.setContentLength(fileByte.length);
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(originalFileName,"UTF-8")+"\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.getOutputStream().write(fileByte);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
	

	@Override
	public void insertComment(Map<String, Object> map, HttpServletRequest request) throws Exception {
		map = sessionHelper.make(map, request);
		noticeDAO.insertComment(map);
	}

	
	
	@Override
	public void deleteComment(Map<String, Object> map) throws Exception {
		noticeDAO.deleteComment(map);
	}




		
}
