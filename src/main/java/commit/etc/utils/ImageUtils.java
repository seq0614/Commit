package commit.etc.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Component("imageUtils")
public class ImageUtils {


	public Map<String, Object> parseInsertMainImg(Map<String, Object> map, HttpServletRequest request)
			throws IllegalStateException, IOException {
		
		String imagePath = request.getSession().getServletContext().getRealPath("/") + "static/uploadImg/";

		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		Iterator<String> iterator = multipartHttpServletRequest.getFileNames();

		MultipartFile multipartFile = null;
		String originalFileName = null;// 원본이름
		String originalFileExtension = null;
		String storedFileName = null;// 파일이 DB에 저장될 이름
		String checkName = null;// form에서 input type 태그의 name값을 확인해주는 역할

		File file = new File(imagePath);
		if (file.exists() == false) {
			file.mkdirs();
		}

		while (iterator.hasNext()) {
			multipartFile = multipartHttpServletRequest.getFile(iterator.next());
			checkName = multipartFile.getName();
			if (checkName.equals("main_img")) {
				if (multipartFile.isEmpty() == false) {
					originalFileName = multipartFile.getOriginalFilename();
					originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
					storedFileName = CommitUtils.getRandomString() + originalFileExtension;
					file = new File(imagePath + storedFileName);
					multipartFile.transferTo(file);
					map.put("MAIN_IMG", storedFileName);
					map.put("ORIGINAL_NAME", originalFileName);
					return map;
				}

			}
		}

		return map;
	}

	public List<Map<String, Object>> parseinsertSubImage(Map<String, Object> map, HttpServletRequest request)
			throws IllegalStateException, IOException {
		
		String imagePath = request.getSession().getServletContext().getRealPath("/") + "static/uploadImg/";

		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		Iterator<String> iterator = multipartHttpServletRequest.getFileNames();

		MultipartFile multipartFile = null;
		String originalFileName = null;
		String originalFileExtension = null;
		String storedFileName = null;
		String checkName = null;
		String step = null;

		List<Map<String, Object>> ImageList = new ArrayList<Map<String, Object>>();

		Map<String, Object> ImageMap = null;

		File file = new File(imagePath);
		if (file.exists() == false) {
			file.mkdirs();
		}

		while (iterator.hasNext()) {// MAIN_IMG, IMG_0, IMG_1, IMG_2
			multipartFile = multipartHttpServletRequest.getFile(iterator.next());
			checkName = multipartFile.getName();
			if (!checkName.equals("main_img")) {
				if (multipartFile.isEmpty() == false) {
					originalFileName = multipartFile.getOriginalFilename();
					originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
					storedFileName = CommitUtils.getRandomString() + originalFileExtension;
					step = checkName.substring(checkName.lastIndexOf("_") + 1);
					file = new File(imagePath + storedFileName);
					multipartFile.transferTo(file);

					ImageMap = new HashMap<String, Object>();
					ImageMap.put("PRO_IDX", map.get("PRO_IDX"));
					ImageMap.put("ORIGINAL_NAME", originalFileName);
					ImageMap.put("STORED_NAME", storedFileName);
					ImageMap.put("STEP", step);

					ImageList.add(ImageMap);
				}

			}

		}

		return ImageList;
	}

	public List<Map<String, Object>> parseUpdateSubImage(Map<String, Object> map, HttpServletRequest request)
			throws IllegalStateException, IOException {
		
		
		String imagePath = request.getSession().getServletContext().getRealPath("/") + "static/uploadImg/";

		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		Iterator<String> iterator = multipartHttpServletRequest.getFileNames();

		MultipartFile multipartFile = null;
		String originalFileName = null;
		String originalFileExtension = null;
		String storedFileName = null;
		String checkName = null;
		String check = null;
		int step = -1;

		List<Map<String, Object>> ImageList = new ArrayList<Map<String, Object>>();

		Map<String, Object> ImageMap = null;


		File file = new File(imagePath);
		if (file.exists() == false) {
			file.mkdirs();
		}

		while (iterator.hasNext()) {// main-img // IMG_IDX
			multipartFile = multipartHttpServletRequest.getFile(iterator.next());
			checkName = multipartFile.getName();
			if (!checkName.equals("main_img")) {
				if (multipartFile.isEmpty() == false) {

					originalFileName = multipartFile.getOriginalFilename();
					originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
					storedFileName = CommitUtils.getRandomString() + originalFileExtension;
					file = new File(imagePath + storedFileName);
					multipartFile.transferTo(file);

					ImageMap = new HashMap<String, Object>();
					ImageMap.put("NEW_FILE", "Y");
					ImageMap.put("PRO_IDX", map.get("PRO_IDX"));
					ImageMap.put("ORIGINAL_NAME", originalFileName);
					ImageMap.put("STORED_NAME", storedFileName);
					ImageMap.put("STEP", step);

					ImageList.add(ImageMap);
				} else {// 파일이 비어있다면 또는 파일을 수정하지 않았다면
					
					check = "sub_img_" + checkName.substring(checkName.lastIndexOf("_")+1);
					if (map.containsKey(check) == true && map.get(check) != null) {
						
						ImageMap = new HashMap<String, Object>();
						ImageMap.put("NEW_FILE", "N");
						ImageMap.put("IMG_IDX", map.get(check));
						ImageMap.put("STEP", step);
						ImageList.add(ImageMap);
					}

				}

			}
			step++;
		}
		return ImageList;
	}
}
