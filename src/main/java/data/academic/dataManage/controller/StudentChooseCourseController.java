package data.academic.dataManage.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import data.academic.dataManage.service.StudentChooseCourseService;
import data.framework.support.AbstractBaseController;
import data.framework.utility.ExcelReadUtils;
import data.framework.utility.UploadHelper;
import data.platform.authority.security.SecurityContext;
@RestController
@RequestMapping("dataManage/studentChooseCourse")
public class StudentChooseCourseController extends AbstractBaseController{

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}
	@Autowired
	private StudentChooseCourseService studentChooseCourseService;
	
	/**
	 * @Title: importStuCodeExamCode
	 * @Description: 导入学籍号考号
	 * @author xiahuajun
	 * @date 2016年9月1日 
	 * @param xlsFile
	 * @param out
	 * @param request
	 * @return void
	 */
	@RequestMapping(params=("command=importStuCodeAndCourse"))
    public void importStuCodeExamCode(@RequestParam("xlsFile")CommonsMultipartFile xlsFile,java.io.PrintWriter out,HttpServletRequest request){
		 //Map<String, Object> requestMap = getSerializer().parseMap(data);
		 File fullFile = new File(uploadPath+File.separator+xlsFile.getFileItem().getName());
		 String fileName = xlsFile.getFileItem().getName();
		 Map<String,Object> paramMap = new HashMap<String,Object>();
		 if(!"xls".equals(fileName.substring(fileName.indexOf(".")+1)) && !"xlsx".equals(fileName.substring(fileName.indexOf(".")+1))){
			 paramMap.put("mess", "fileFormatError");
			 out.print(getSerializer().formatMap(paramMap));
			 return;
		 }
		 try {
			 if(!fullFile.exists())
			 {
				 fullFile.createNewFile();
			 }
			xlsFile.transferTo(fullFile);
			List<List<Object>> list =ExcelReadUtils.readExcel(fullFile);
			if(list.size() == 0){
				paramMap.put("mess", "noData");
				out.print(getSerializer().formatMap(paramMap));
				return;
			}
			//System.out.println(list.size());
			
			//List<Object> titleO = list.get(0);
			List<Map<String,Object>> paramList = new ArrayList<Map<String,Object>>();
			String create_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			String create_person = SecurityContext.getPrincipal().getChineseName();
			for (int i = 1; i < list.size(); i++) {
				List<Object> rowData = list.get(i);
				if (rowData.isEmpty() || rowData.get(0).equals("") || rowData.get(0) == null) {
					break;
				}
				//for(int k = 0; k < rowData.size(); k++){。
					Map<String,Object> param = new HashMap<String,Object>();
					//param.put("exam_number", rowData.get(0));
					//param.put("name", rowData.get(1));
					//param.put("tihao", titleO.get(k));
					//param.put("score", rowData.get(k));
					
					param.put("studentCode", rowData.get(0));
					if("语文".equals(rowData.get(1))){
						param.put("course","yw");
					}
					else if("数学".equals(rowData.get(1))){
						param.put("course","sx");
					}
					else if("地理".equals(rowData.get(1))){
						param.put("course","dl");
					}
					else if("化学".equals(rowData.get(1))){
						param.put("course","hx");
					}
					
					else if("历史".equals(rowData.get(1))){
						param.put("course","ls");
					}
					else if("美术".equals(rowData.get(1))){
						param.put("course","ms");
					}
					else if("牛津英语".equals(rowData.get(1))){
						param.put("course","njyy");
					}
					else if("生物".equals(rowData.get(1))){
						param.put("course","sw");
					}
					else if("思想政治".equals(rowData.get(1))){
						param.put("course","sxzz");
					}
					else if("体育".equals(rowData.get(1))){
						param.put("course","ty");
					}
					else if("拓展型课程".equals(rowData.get(1))){
						param.put("course","tzxkc");
					}
					else if("物理".equals(rowData.get(1))){
						param.put("course","wl");
					}
					else if("高中新世纪英语".equals(rowData.get(1))){
						param.put("course","xsjyy");
					}
					else if("信息科技".equals(rowData.get(1))){
						param.put("course","xxkj");
					}
					else if("研究型课程".equals(rowData.get(1))){
						param.put("course","yjxkc");
					}
					else if("音乐".equals(rowData.get(1))){
						param.put("course","yyue");
					}
					else {
						 paramMap.put("mess", "courseNotMatch");
						 out.print(getSerializer().formatMap(paramMap));
						 return;
					}
					param.put("create_time", create_time);
					param.put("create_person", create_person);
					
					//map.put("schoolYear",requestMap.get("schoolYear").toString());
					
					param.put("randomUUid", UUID.randomUUID().toString());
					//param.put("course", map.get("course"));
					paramList.add(param);
				
			}
			//导入学籍号科目	
			studentChooseCourseService.insertStuCodeAndCourse(paramList);
			paramMap.put("mess", "success");
			out.print(getSerializer().formatMap(paramMap));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		}
		
	}	 
	
	//@RequestMapping(params=("command=downloadExcelTemplete"))
	/*public void download(HttpServletRequest request,  
            HttpServletResponse response,java.io.PrintWriter out1) throws Exception {  
        response.setContentType("text/html;charset=UTF-8");   
        FileInputStream in = null;  
        PrintWriter out = null;  
//        request.setCharacterEncoding("UTF-8");  
        String path = request.getSession().getServletContext().getRealPath(  
                "/");  
//        String fileName = request.getParameter("fileName");  
        //fileName=CommonProperty.getValue(fileName);  
        try {  
        	String fileName = request.getParameter("fileName");  //23239283-92489-阿凡达.avi
            fileName = new String(fileName.getBytes("iso8859-1"),"UTF-8");
            //上传的文件都是保存在/WEB-INF/upload目录下的子目录当中
//            String fileSaveRootPath=this.getServletContext().getRealPath("/WEB-INF/upload");
            //通过文件名找出文件的所在目录
//            String path = findFileSavePathByFileName(fileName,fileSaveRootPath);
            //得到要下载的文件
//            File file = new File(path + "\\" + fileName);
            File file = new File(path + "download/" + fileName);
            //如果文件不存在
            if(!file.exists()){
                request.setAttribute("message", "您要下载的资源已被删除！！");
                request.getRequestDispatcher("/message.jsp").forward(request, response);
                return;
            }
        	
        	  
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
          //读取要下载的文件，保存到文件输入流
             in = new FileInputStream(file);
          //创建输出流
            out = response.getWriter();
            //创建缓冲区
            byte buffer[] = new byte[1024];
            int len = 0;
            //循环将输入流中的内容读取到缓冲区当中
            while((len=in.read(buffer))>0){
                //输出缓冲区的内容到浏览器，实现文件下载
                out.write(len);
            }
//            //关闭文件输入流
//            in.close();
//            //关闭输出流
//            out.close();
            
            
            
            
//            response.setContentType("application/x-excel");  
//            response.setCharacterEncoding("UTF-8");  
//              response.setHeader("Content-Disposition", "attachment; filename="+fileName);  
//            response.setHeader("Content-Length",String.valueOf(f.length()));  
//            in = new BufferedInputStream(new FileInputStream(f));  
//            out = new BufferedOutputStream(response.getOutputStream());
////            out = new BufferedOutputStream(new FileOutputStream("c:/dd.xls"));
//            byte[] data = new byte[1024];  
//            int len = 0;  
//            while (-1 != (len=in.read(data, 0, data.length))) {  
//                out.write(data, 0, len);  
//            }
//            Map<String,Object> map = new HashMap<String,Object>();
//            map.put("mess", "success");
//            out1.print(getSerializer().formatMap(map));
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (in != null) {  
                in.close();  
            }  
            if (out != null) {  
                out.close();  
            }  
        }  
  
    }*/  
    private static String uploadPath = UploadHelper.getRealUploadPath() ;
}
