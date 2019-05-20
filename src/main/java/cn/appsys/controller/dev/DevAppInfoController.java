package cn.appsys.controller.dev;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import cn.appsys.constant.Constants;
import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.pojo.DevUser;
import cn.appsys.service.dev.appcategory.AppCategoryService;
import cn.appsys.service.dev.appinfo.DevAppInfoService;
import cn.appsys.service.dev.appversion.AppVersionService;
import cn.appsys.service.dev.datadictionary.DataDictionaryService;
import cn.appsys.tools.Page;

@Api
@Controller
@RequestMapping("/dev/appinfo")
public class DevAppInfoController {

	@Resource
	private DevAppInfoService deveAppInfoService;

	@Resource
	private DataDictionaryService dataDictionaryService;

	@Resource
	private AppCategoryService appCategoryService;

	@Resource
	private AppVersionService appVersionService;

	/**
	 * 分页并按条件查询app列表信息
	 * 
	 * @param model
	 *            模型对象
	 * @param querySoftwareName
	 *            软件名称
	 * @param queryStatus
	 *            APP状态
	 * @param queryFlatformId
	 *            所属平台
	 * @param queryCategoryLevel1
	 *            一级分类
	 * @param queryCategoryLevel2
	 *            二级分类
	 * @param queryCategoryLevel3
	 *            三级分类
	 * @param currPageNo
	 *            当前页码
	 * @return 返回的是app列表信息
	 */
	@RequestMapping("/list")
	public String list(
			Model model,
			@RequestParam(value = "querySoftwareName", required = false) String querySoftwareName,
			@RequestParam(value = "queryStatus", required = false) Integer queryStatus,
			@RequestParam(value = "queryFlatformId", required = false) Integer queryFlatformId,
			@RequestParam(value = "queryCategoryLevel1", required = false) Integer queryCategoryLevel1,
			@RequestParam(value = "queryCategoryLevel2", required = false) Integer queryCategoryLevel2,
			@RequestParam(value = "queryCategoryLevel3", required = false) Integer queryCategoryLevel3,
			@RequestParam(value = "currPageNo", required = false) Integer currPageNo) {

		// 获取分页查询所需要的总记录数
		int totalCount = deveAppInfoService.queryCount(querySoftwareName,
				queryStatus, queryFlatformId, queryCategoryLevel1,
				queryCategoryLevel2, queryCategoryLevel3);
		int pageSize = 5; // 每页显示5行数据
		currPageNo = currPageNo != null ? currPageNo : 1; // 当前页码
		Page pages = new Page();
		pages.setCurrPageNo(currPageNo);
		pages.setPageSize(pageSize);
		pages.setTotalCount(totalCount);

		int from = (currPageNo - 1) * pageSize; // 获取分页SQL所需要的位置偏移量。
		List<AppInfo> appInfoList = deveAppInfoService.queryAppPageInfo(
				querySoftwareName, queryStatus, queryFlatformId,
				queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3,
				from, pageSize); // 分页查询app列表信息

		List<DataDictionary> statusList = dataDictionaryService
				.queryByTypeCode("APP_STATUS");// App状态列表
		List<DataDictionary> flatFormList = dataDictionaryService
				.queryByTypeCode("APP_FLATFORM"); // 所属平台列表
		List<AppCategory> categoryLevel1List = appCategoryService
				.queryById(null); // 一级分类
		List<AppCategory> categoryLevel2List = null; // 二级分类列表
		List<AppCategory> categoryLevel3List = null; // 三级分类列表
		if (queryCategoryLevel2 != null && queryCategoryLevel2 != 0) {
			// 查询二级分类信息
			categoryLevel2List = appCategoryService
					.queryById(queryCategoryLevel2);
			model.addAttribute("categoryLevel2List", categoryLevel2List);
		}
		if (queryCategoryLevel3 != null && queryCategoryLevel3 != 0) {
			// 查询三级分类信息
			categoryLevel3List = appCategoryService
					.queryById(queryCategoryLevel3);
			model.addAttribute("categoryLevel3List", categoryLevel3List);
		}
		model.addAttribute("statusList", statusList);
		model.addAttribute("flatFormList", flatFormList);
		model.addAttribute("categoryLevel1List", categoryLevel1List);// 得到的是一级分类列表
		model.addAttribute("categoryLevel2List", categoryLevel2List);// 得到的是二级分类列表
		model.addAttribute("categoryLevel3List", categoryLevel3List); // 得到的是三级分类列表
		model.addAttribute("appInfoList", appInfoList);
		model.addAttribute("pages", pages);
		// 将查询条件也保存到模型中
		model.addAttribute("querySoftwareName", querySoftwareName);
		model.addAttribute("queryStatus", queryStatus);
		model.addAttribute("queryFlatformId", queryFlatformId);
		model.addAttribute("queryCategoryLevel1", queryCategoryLevel1);
		model.addAttribute("queryCategoryLevel2", queryCategoryLevel2);
		model.addAttribute("queryCategoryLevel3", queryCategoryLevel3);

		return "developer/appinfolist"; // 跳转app信息列表页面
	}

	// 根据parentId到app_category表去查询相关的分类列表信息, pid就是ParentId
	@RequestMapping(value = "/queryCategoryListByParentId", method = RequestMethod.GET)
	@ResponseBody
	public Object queryCategoryListByParentId(@RequestParam("pid") Integer pid) {
		List<AppCategory> categoryList = appCategoryService.queryById(pid);
		return categoryList;
	}

	// 下面方法只负责跳转到app基础信息新增页面
	@RequestMapping("/appinfoadd")
	public String appinfoadd() {
		return "developer/appinfoadd";
	}

	// 根据typeCode到app_datadictionary字典表中查询相关信息,可以查询[所属平台]、[用户类型]、[APP状态]、[发布状态]
	@RequestMapping(value = "/getDataDictionaryByCode", method = RequestMethod.GET)
	@ResponseBody
	public Object getDataDictionaryByCode(@RequestParam("tcode") String tcode) {
		List<DataDictionary> dictionaryList = dataDictionaryService
				.queryByTypeCode(tcode);
		return dictionaryList;
	}

	// 在新增操作时，对APKName进行唯一验证
	@RequestMapping("/apkexist")
	@ResponseBody
	public Object apkexist(@RequestParam("APKName") String APKName) {
		Map<String, String> map = new HashMap<String, String>();
		if (APKName == null || ("").equals(APKName)) {
			map.put("APKName", "empty"); // 在js中进行非空验证
		} else {
			AppInfo info = deveAppInfoService
					.queryInfoByIdAndAPK(null, APKName);
			if (info != null) {
				map.put("APKName", "exist"); // 说明数据库中存在
			} else {
				map.put("APKName", "noexist");
			}
		}
		return map;
	}


	// 负责完成最终的新增app基础信息操作
	@ApiOperation(
			value="负责完成最终的新增app基础信息操作",protocols = "HTTP",
			response = String.class, httpMethod = "GET",
			notes = "调用业务层的insertAppInfo方法返回int类型值，取值分别是：" +
					"<p>result > 0时，新增成功跳转到app列表信息页面</p>" +
					"<p>否则，新增失败又跳转回新增页面</p>" )
	@RequestMapping("/appinfoaddsave")
	public String appAddSave(HttpSession session, HttpServletRequest request,
							 @RequestBody AppInfo appInfo, Model model,
							 @RequestParam("a_logoPicPath") MultipartFile attach) {
		String logoPicPath = ""; // 用于保存图片相对路径格式如：/AppInfoSystem/statics/uploadfiles/com.lbe.security.jpg
		String logoLocPath = ""; // 用于保存图片的绝对路径
		if (!attach.isEmpty()) { // 不为空的情况，我们进行文件上传 statics\\uploadfiles
			// path的值格式为：G:\\tool\\01-zip\\apache-tomcat-7.0.62\\webapps\\AppInfoSystem\\statics\\uploadfiles
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + File.separator + "uploadfiles");// 获取文件上传所需要的路径。
			String oldFileName = attach.getOriginalFilename(); // 得到上传图片的原文件名
			String suffix = FilenameUtils.getExtension(oldFileName);// 得到原文件的后缀
			if (attach.getSize() > 5000000) { // 如果上传文件的大小超过500kb
				model.addAttribute("fileUploadError", "* 上传文件的大小超过500kb，上传失败！");
			} else if (suffix.equalsIgnoreCase("jpg")
					|| suffix.equalsIgnoreCase("png")
					|| suffix.equalsIgnoreCase("jpeg")
					|| suffix.equalsIgnoreCase("pneg")) {
				// 判断用户上传的文件格式是否是jpg、png、jpeg、pneg格式
				String fileName = appInfo.getAPKName() + ".jpg";// 获取上传之后的文件名字，因为
																// APKName是唯一
				File file = new File(path, fileName); // 根据文件对象
				if (!file.exists()) { // 如果文件对象不存在，那么就创建这个目录
					file.mkdirs(); // 创建这个目录
				}
				try {
					attach.transferTo(file);// 将attach上传到指定的目录中去
				} catch (IllegalStateException | IOException e) {
					model.addAttribute("fileUploadError", "* 文件上传失败");
					e.printStackTrace();
				}
				logoPicPath = request.getContextPath()
						+ "/statics/uploadfiles/" + fileName; // 用于保存图片相对路径格式如：/AppInfoSystem/statics/uploadfiles/com.lbe.security.jpg
				logoLocPath = path + File.separator + fileName; // 保存的是图片的绝对路径
			}
		}
		// 在进行新增操作时，createdBy及creationDate赋值
		appInfo.setCreatedBy(((DevUser) session
				.getAttribute(Constants.DEV_USER_SESSION)).getId()); // 创建者的用户id
		appInfo.setCreationDate(new Date()); // 新增时间
		appInfo.setLogoLocPath(logoLocPath);
		appInfo.setLogoPicPath(logoPicPath);
		int result = deveAppInfoService.insertAppInfo(appInfo);
		if (result > 0) {
			return "redirect:/dev/appinfo/list"; // 新增成功跳转到app列表信息页面
		} else {
			return "developer/appinfoadd"; // 新增失败又跳转回新增页面
		}
	}

	// 在appinfolist.jsp页面中点击[修改]链接，按id查询app的详情信息，
	// 并跳转到修改页面，将详情信息进行显示
	@RequestMapping("/appinfomodify/{id}")
	public String appinfomodify(@PathVariable Integer id, Model model) {
		// 通过传递过来的appinfo的id，将appinfo的详情查询给查询出来，并赋值appInfo对象。
		AppInfo appInfo = deveAppInfoService.queryInfoByIdAndAPK(id, null);// 按id查询app列表详情信息
		// appInfo对象保存到模型中。
		model.addAttribute("appInfo", appInfo);
		return "developer/appinfomodify";
	}

	// 在修改appinfo过程中，当点击[删除]链接跳转到当前方法中。  在修改appinfo时，删除LOGO图片
	@ApiOperation(
			value="根据appinfo主键的id查询appinfo详情对象信息，将此对象中的LOGO图片删除",
			httpMethod = "GET", protocols = "HTTP",produces = "application/json",
			response = Object.class,
			notes="<p>返回的值是一个Map集合，key都为result，值的取值如下/p>" +
					"<p>value='failed' 表示删除失败！</p>" +
					"<p>value='success' 表示删除成功！</p>" +
					"<p>value='noexist' 表示要删除的LOGO图片不存在！</p>" )
	@RequestMapping(value = "/delfile", method = RequestMethod.GET)
	@ResponseBody
	public Object delfile(@ApiParam(required = true, name="id" ,value="appinfo主键的id") @RequestParam("id") Integer id) {// id为appinfo的主键id

		Map<String, String> map = new HashMap<String, String>();
		if (id == null) {
			map.put("result", "failed"); // 删除失败
		} else {
			// 根据appinfo主键的id查询appinfo详情对象信息
			AppInfo appInfo = deveAppInfoService.queryInfoByIdAndAPK(id, null);
			String logoLocPath = appInfo.getLogoLocPath(); // 得到上传图片的绝对路径。
			File file = new File(logoLocPath);// 根据图片的绝对路径创建一个File对象。
			if (file.exists()) { // 说明file对象或者指定目录下面有那个图片文件，如果存在就把物理文件删除
				if (file.delete()) { // 如果返回true，表示将物理文件从服务器上面删除成功
					// 将数据库中app_info表里指定的logoPicPath和logoLocPath两个列的值给清空
					if (deveAppInfoService.deleteLogPath(id)) {
						map.put("result", "success"); // 表示整个删除操作执行成功
					}
				}
			} else { // 表示不存在
				map.put("result", "noexist");
			}
		}
		return map;
	}

	// 实现保存最终的修改操作, 图片上传
	@RequestMapping(value = "/appinfomodifysave", method = RequestMethod.POST)
	public String appInfoModifySave(HttpSession session,
			HttpServletRequest request,
			@RequestParam("attach") MultipartFile attach, Model model,
			AppInfo appInfo) {
		String logoLocPath = ""; // 上传图片的绝对路径
		String logoPicPath = ""; // 上传图片的相对路径
		if (!attach.isEmpty()) { // 多组件对象是否为空。
			// 获取文件上传所需要的路径
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + File.separator + "uploadfiles");
			String oldFileName = attach.getOriginalFilename(); // 得到原文件名
			String suffix = FilenameUtils.getExtension(oldFileName); // 原文件的后缀
			if (attach.getSize() > 5000000) {
				model.addAttribute("fileUploadError", "* 上传文件的大小超过500KB，上传失败！");
			} else if (suffix.equalsIgnoreCase("jpg")
					|| suffix.equalsIgnoreCase("png")
					|| suffix.equalsIgnoreCase("jepg")
					|| suffix.equalsIgnoreCase("pneg")) {// 上传图片格式
				String fileName = appInfo.getAPKName() + ".jpg";// 上传LOGO图片命名:apk名称.apk
				File file = new File(path, fileName);
				if (!file.exists()) { // 如果file对象不存在，那么就在指定目录下创建指定的文件
					file.mkdirs();
				}
				try {
					attach.transferTo(file);
				} catch (IllegalStateException | IOException e) {
					model.addAttribute("fileUploadError", "* 文件上传失败");
					e.printStackTrace();
				}
				logoPicPath = request.getContextPath()
						+ "/statics/uploadfiles/" + fileName;
				logoLocPath = path + File.separator + fileName;
			}
		}

		appInfo.setModifyBy(((DevUser) session
				.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setModifyDate(new Date());
		appInfo.setLogoLocPath(logoLocPath);
		appInfo.setLogoPicPath(logoPicPath);
		try {
			if (deveAppInfoService.modifyAppInfo(appInfo)) { // true修改成功，false修改失败
				return "redirect:/dev/appinfo/list";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "developer/appinfomodify"; // 如果是修改失败，就重新跳转修改页面
	}

	// 新增版本之前，按照appinfo的id将当前app的历史版本列表信息给查询出来。
	@RequestMapping("/appversionadd/{id}")
	public String appVersionAdd(@PathVariable Integer id, Model model) {
		// 根据appinfo的id去查询当前app的历史版本列表信息(app_version)
		List<AppVersion> appVersionList = appVersionService
				.queryAppVersionListByAppId(id);
		AppVersion appVersion = new AppVersion();
		appVersion.setAppId(id); // 将从appinfolist.jsp传递过来的appinfo的id保存到appVersion对象中，传递到appversionadd.jsp，用于后面的新增操作。
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appVersionList", appVersionList);
		return "developer/appversionadd"; // 跳转到app版本的新增页面。
	}

	// 完成最后新增版本操作
	@RequestMapping(value = "/addversionsave", method = RequestMethod.POST)
	public String addVersionSave(HttpServletRequest request,
			HttpSession session, AppVersion appVersion, Model model,
			@RequestParam("a_downloadLink") MultipartFile attach) {

		String downloadLink = ""; // 用于保存apk上传的下载链接即app_version表中的downloadlink字段。
		String apkLocPath = ""; // 用于保存apk文件的绝对路径
								// 。新增操作时，将这个绝对路径新增到apkLocPath字段中。
		String apkFileName = ""; // 用于保存apk文件的名字
		if (!attach.isEmpty()) { // 表示此时进行文件上传操作
			// 得到apk文件上传所需要的路径。即要将apk文件上传到服务器哪个目录中去。
			// 路径的格式如：G:\\tool\\01-zip\\apache-tomcat-7.0.62\\webapps\\AppInfoSystem_01\\statics\\uploadfiles\\
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + File.separator + "uploadfiles");
			String oldFileName = attach.getOriginalFilename();// 获取原apk文件的名字
			String suffix = FilenameUtils.getExtension(oldFileName);// 获取原文件的后缀
			if (attach.getSize() > 5000000) { // 500kb
				model.addAttribute("fileUploadError",
						"* 上传的apk文件大小，超过了500kb，上传失败！");
			} else if (suffix.equalsIgnoreCase("apk")) { // 说明上传文件是apk文件，符合我们对上传文件的要求。
				AppInfo appInfo = deveAppInfoService.queryInfoByIdAndAPK(
						appVersion.getAppId(), null);// 通过appid查询app详情信息
				String apkName = appInfo.getAPKName() + appInfo.getVersionNo()
						+ ".apk";// 重新命名apk的名字,命名规则：apk名称+版本号+.apk
				File targetFile = new File(path, apkName);// 创建一个File对象。
				if (!targetFile.exists()) {
					targetFile.mkdirs(); // 如果file对象不存在，就创建这个文件
				}
				try {
					attach.transferTo(targetFile); // 将apk文件上传到指定目录中
				} catch (IllegalStateException | IOException e) {
					model.addAttribute("fileUploadError", "* 上传失败！");
					e.printStackTrace();
				}

				downloadLink = request.getContextPath()
						+ "/statics/uploadfiles/" + apkName; // 下载链接
				apkLocPath = path + File.separator + apkName; // 在服务器上面apk文件的绝对路径
				apkFileName = apkName;
			}
		}
		// 再给app_version表中modifyBy与modifyDate、downloadlink、apkLocPath、apkFileName字段赋值
		appVersion.setCreatedBy(((DevUser) session
				.getAttribute(Constants.DEV_USER_SESSION)).getId()); // 修改者的编号
		appVersion.setCreationDate(new Date()); // 修改时间
		appVersion.setDownloadLink(downloadLink);
		appVersion.setApkFileName(apkFileName);
		appVersion.setApkLocPath(apkLocPath);
		if (appVersionService.addAppVersion(appVersion)) { // 执行新增操作后，appVersion对象中的id属性就会有值。
			AppInfo info = new AppInfo();
			info.setId(appVersion.getAppId()); // 给AppInfo对象的id赋值
			info.setVersionId(appVersion.getId());
			deveAppInfoService.modifyAppInfo(info); // 调用更新方法，实现按appinfo的id去更新app_info表中的version_id列的值
			return "redirect:/dev/appinfo/list"; // 一旦新增成功就跳转到app列表信息页面
		} else {
			// 如果新增失败再跳回新增页面，并显示当前app的历史版本列表信息
			return "redirect:/dev/appinfo/appversionadd/"
					+ appVersion.getAppId();
		}
	}

	/**
	 * 修改前，按appinfo的id去查询当前app的历史版本列表信息以及按版本id去查询当前版本的详情对象信息
	 * @param vid 版本id
	 * @param aid appinfo的主键id
	 * @return  app的历史版本列表信息以及当前版本的详情对象信息
	 */
	@RequestMapping("/appversionmodify")
	public String appVersionModify(Model model,
			@RequestParam(value = "vid", required = false) Integer vid,
			@RequestParam(value = "aid", required = false) Integer aid) {
		//根据appinfo的id去查询当前app的历史版本列表信息
		List<AppVersion> appVersionList = appVersionService.queryAppVersionListByAppId(aid);
		//按版本id去查询当前版本的详情对象信息
		AppVersion appVersion = appVersionService.getAppVersionByVid(vid);
		model.addAttribute("appVersionList",appVersionList);
		model.addAttribute("appVersion",appVersion);
		return "developer/appversionmodify";  //跳转到修改页面
	}
	
	@RequestMapping(value = "/delapkfile", method = RequestMethod.GET)
	@ResponseBody
	public Object delapkfile(@RequestParam("id") Integer id) {// id为app_version的主键id，即版本id
		Map<String, String> map = new HashMap<String, String>();
		if (id == null) {
			map.put("result", "failed"); // 删除失败
		} else {
			// 根据appinfo主键的id查询appinfo详情对象信息
			AppVersion appVersion = appVersionService.getAppVersionByVid(id); //通过版本id查询版本对象信息
			String apkLocPath = appVersion.getApkLocPath(); // 得到上传图片的绝对路径。
			File file = new File(apkLocPath);// 根据图片的绝对路径创建一个File对象。
			if (file.exists()) { // 说明file对象或者指定目录下面有那个图片文件，如果存在就把物理文件删除
				if (file.delete()) { // 如果返回true，表示将物理文件从服务器上面删除成功
					// 将数据库中app_version表里指定的apkLocPath、downloadLink、apkFileName三个列的值给清空
					if (appVersionService.deleteApkPath(id)) {
						map.put("result", "success"); // 表示整个删除操作执行成功
					}
				}
			} else { // 表示不存在
				map.put("result", "noexist");
			}
		}
		return map;
	}
}