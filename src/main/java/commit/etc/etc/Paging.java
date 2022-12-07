package commit.etc.etc;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service("paging")
public class Paging {											//한 페이지당 보여줄 수
	public Map<String, Object> calc(Map<String, Object> map, int recordCountPerPage) {
		//총 출력할 데이터 개수
		int totalRecord = Integer.parseInt(map.get("totalRecord").toString());
		//현재 페이지
		int currentPage = map.get("currentPage") == null ? 1 : Integer.parseInt(map.get("currentPage").toString());
		
	
		//전체 페이지 수
		int totalPage = totalRecord / recordCountPerPage + (totalRecord % recordCountPerPage == 0 ? 0 : 1);
		
		int startRow = 1 + (currentPage - 1) * recordCountPerPage;
		int endRow = currentPage * recordCountPerPage;
		if (totalRecord < endRow) {
			endRow = totalRecord;
		}

		boolean prev = currentPage - 5 >= 1;
		boolean next = ((currentPage - 1) / 5) < ((totalPage - 1) / 5);
		
		map.put("START_ROW", startRow);
		map.put("END_ROW", endRow);
		map.put("PREV", prev);
		map.put("NEXT", next);
		map.put("TOTALPAGE", totalPage);

		return map;
	}

}
