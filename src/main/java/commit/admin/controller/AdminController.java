package commit.admin.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import commit.admin.service.AdminService;
import commit.etc.etc.CommitMap;
import commit.my.service.MyService;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    Logger log = Logger.getLogger(this.getClass());

    @Resource(name = "adminService")
    private AdminService adminService;

    @Resource(name = "myService")
    private MyService myService;

    @RequestMapping(value = "/main")
    public String main() {

        return "forward:/admin/order/list";
    }

    //주문목록
    @GetMapping(value = "/order/list")
    public String orderList() throws Exception {

        return "/admin/order/list";
    }

    @ResponseBody
    @PostMapping(value = "/order/list/data")
    public List<Map<String, Object>> orderListData(@RequestBody Map<String, Object> map) throws Exception {
        return adminService.getOrderList(map);
    }

    @PostMapping(value = "/order/update")
    @ResponseBody
    public void orderUpdate(@RequestBody Map<String, Object> map) throws Exception {
        adminService.updateOrder(map);
    }

    @RequestMapping(value = "/order/detail")
    public ModelAndView orderDetail(CommitMap commitMap, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Map<String, Object> map = myService.orderDetail(commitMap.getMap(), request, response);
        ModelAndView mv = new ModelAndView();
        mv.addObject("order", map.get("order"));
        mv.addObject("orderInfo", map.get("orderInfo"));
        mv.setViewName("/admin/order/detail");
        return mv;

    }

    //회원목록
    @GetMapping(value = "/member/list")
    public String memList(CommitMap commitMap) throws Exception {

        return "/admin/member/list";
    }


    @ResponseBody
    @PostMapping(value = "/member/list/data")
    public List<Map<String, Object>> memListdata(@RequestBody Map<String, Object> map) throws Exception {

        List<Map<String, Object>> list = adminService.selectMemberList(map);

        return list;
    }


    @RequestMapping(value = "/member/detail")
    public ModelAndView memDetail(CommitMap commitMap, HttpServletRequest request) throws Exception {

        Map<String, Object> map = adminService.selectMemberDetail(commitMap.getMap());
        ModelAndView mv = new ModelAndView("/admin/member/detail");
        mv.addObject("memberInfo", map);

        return mv;
    }

    @RequestMapping(value = "/member/delete")
    public ModelAndView memDel(@RequestBody Map<String, Object> map) throws Exception {
        ModelAndView mv = new ModelAndView("redirect:/admin/member/list");
        adminService.deleteMember(map);
        return mv;
    }

    //상품목록
    @GetMapping(value = "/pro/list")
    public String proList(CommitMap commitMap) throws Exception {
        return "/admin/pro/list";
    }

    @ResponseBody
    @PostMapping(value = "/pro/list/data")
    public List<Map<String, Object>> proList(@RequestBody Map<String, Object> map) throws Exception {
        List<Map<String, Object>> list = adminService.selectProList(map);
        return list;
    }

    @RequestMapping(value = "/pro/add", method = RequestMethod.GET)
    public String proAddForm() {
        return "/admin/pro/addForm";
    }

    @RequestMapping(value = "/pro/add", method = RequestMethod.POST)
    public ModelAndView proAdd(CommitMap commitMap, HttpServletRequest request) throws Exception {

        ModelAndView mv = new ModelAndView();

        adminService.insertPro(commitMap.getMap(), request);

        // 상품 등록후 등록한 상품 디테일컷으로(클라이언트 쪽으로 보이는 상품 디테일컷으로) 이동
        mv.addObject("PRO_IDX", commitMap.get("PRO_IDX"));
        mv.setViewName("redirect:/pro/detail");

        return mv;
    }


    @RequestMapping(value = "/pro/update", method = RequestMethod.GET)
    public ModelAndView proUpdateForm(CommitMap commitMap) throws Exception {

        ModelAndView mv = new ModelAndView("/admin/pro/updateForm");
        Map<String, Object> map = adminService.detailPro(commitMap.getMap());
        mv.addObject("proInfo", map.get("proInfo"));
        mv.addObject("proImage", map.get("proImage"));
        return mv;

    }

    @RequestMapping(value = "/pro/update/data/{PRO_IDX}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> proData(@PathVariable String PRO_IDX, CommitMap commitMap) throws Exception {
        commitMap.put("PRO_IDX", PRO_IDX);
        // 디테일 프로에서는 상품 정보와 상품 이미지들을 가져와야함.
        Map<String, Object> map = adminService.detailPro(commitMap.getMap());
        return map;
    }

    // 수정하고 나서 수정한 해당상품 디테일로 이동
    @RequestMapping(value = "/pro/update", method = RequestMethod.POST)
    public String proUpdate(CommitMap commitMap, HttpServletRequest request) throws Exception {
        adminService.updatePro(commitMap.getMap(), request);

        return "redirect:/admin/pro/list";

    }

    @RequestMapping(value = "/pro/delete")
    @ResponseBody
    public void proDelete(@RequestBody Map<String, Object> map, HttpServletResponse response) throws Exception {

        adminService.deletePro(map, response);

    }

}
