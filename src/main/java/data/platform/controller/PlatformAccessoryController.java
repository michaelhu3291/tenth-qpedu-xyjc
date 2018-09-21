package data.platform.controller ;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import data.framework.data.DataSerializer;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.framework.utility.FileHelper;
import data.framework.utility.UploadHelper;
import data.platform.entity.EntityPlatformAccessory;
import data.platform.service.PlatformAccessoryService;

@Controller
@RequestMapping( "platform/accessory_" )
public class PlatformAccessoryController extends AbstractBaseController
{
    @Override
    protected void init( ModelMap model, HttpServletRequest request )
    {
    }
    @RequestMapping( params = "command=upload" )
    public void upload( HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "file")CommonsMultipartFile[] files) throws Exception {
        FileItemFactory factory = new DiskFileItemFactory();
         ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding(charSetName);
        Calendar today = Calendar.getInstance();
        String year = String.valueOf(today.get(Calendar.YEAR));
        String month = String.valueOf(today.get(Calendar.MONTH) + 1);
        
        String upPath = uploadPath + File.separator + year + File.separator + month + File.separator;
        File uploadFolder = new File(upPath);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        List<FileItem> items =new ArrayList<FileItem>();
        for (int i = 0; i < files.length; i++) {
			CommonsMultipartFile commonsMultipartFile = files[i];
			items.add(commonsMultipartFile.getFileItem());
		}
        
        
        for (FileItem item : items) {
            String fileName = item.getName();
            if (fileName != null && fileName.length() != 0) {
                // 上传文件
                File fullFile = new File(fileName);
                String realName = fullFile.getName();
                String realExtName = FileHelper.getExtFromFileName(realName);
                String serverName = UploadHelper.createFileName(request, realExtName);
                String serverPath = upPath + serverName;
                File savedFile = new File(serverPath);
                item.write(savedFile);

                // 持久化文件信息
                EntityPlatformAccessory fileEntity = new EntityPlatformAccessory();
                fileEntity.setFileName(realName);
                fileEntity.setFileNameExtension(realExtName);
                fileEntity.setFileNameInServer(serverName);
                fileEntity.setFilePathInServer(savedFile.getPath());
                fileEntity.setFileSize(item.getSize());
                //SecurityContext.getPrincipal().getId();
                fileEntity.setOperator("");
                accessoryService.save(fileEntity);
                Map<String, String> map = new HashMap<String, String>();
                map.put("fileName", realName);
                System.out.println("filePath" + realName);
                map.put("path", "");
                map.put("id", fileEntity.getId());
                map.put("filePathInServer", fileEntity.getFilePathInServer());
                map.put("fileNameExtension", fileEntity.getFileNameExtension());
                result.add(map);
            }
        }
        
        String oie = request.getParameter("oie");
        if (StringUtils.isNotBlank(oie)) {
            response.getWriter().write("<html><head><meta http-equiv='Content-Type' content='text/html;charset=UTF-8'></head><body>"+serializer.formatList(result)+"</body></html>");
        } else {
            response.getWriter().write(serializer.formatList(result));
        }
        
    }
   
    @RequestMapping( params = "command=uploadById" )
    public void uploadById( HttpServletRequest request, HttpServletResponse response ) throws Exception {
    	
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding(charSetName);
        
        Calendar today = Calendar.getInstance();
        String year = String.valueOf(today.get(Calendar.YEAR));
        String month = String.valueOf(today.get(Calendar.MONTH) + 1);
        
        String upPath = uploadPath + File.separator + year + File.separator + month + File.separator;
        File uploadFolder = new File(upPath);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        List<FileItem> items = upload.parseRequest(request);
        for (FileItem item : items) {
            String fileName = item.getName();
            if (fileName != null && fileName.length() != 0) {
                // 上传文件
            	EntityPlatformAccessory fileEntity = accessoryService.load(request.getParameter("id"));
                String serverPath = upPath + fileEntity.getFileNameInServer();
                File savedFile = new File(serverPath);
                item.write(savedFile);

                // 持久化文件信息
                fileEntity.setFilePathInServer(savedFile.getPath());
                fileEntity.setFileSize(item.getSize());
                accessoryService.update(fileEntity);
                Map<String, String> map = new HashMap<String, String>();
                map.put("fileName", fileEntity.getFileName());
                System.out.println("filePath" + fileEntity.getFileName());
                map.put("path", "");
                map.put("id", fileEntity.getId());
                map.put("filePathInServer", fileEntity.getFilePathInServer());
                map.put("fileNameExtension", fileEntity.getFileNameExtension());
                result.add(map);
            }
        }
        String oie = request.getParameter("oie");
        if (StringUtils.isNotBlank(oie)) {
            response.getWriter().write("<html><head><meta http-equiv='Content-Type' content='text/html;charset=UTF-8'></head><body>"+serializer.formatList(result)+"</body></html>");
        } else {
            response.getWriter().write(serializer.formatList(result));
        }
        
    }
    
    
    
    
    @RequestMapping( params = "command=download" )
    public void download( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        String fileId = request.getParameter( "id" ) ;
        if( fileId != null && fileId.length() != 0 )
        {
            EntityPlatformAccessory fileEntity = accessoryService.load( fileId ) ;
            if( fileEntity != null )
            {
                UploadHelper.sendFile(
                        response,
                        fileEntity.getFilePathInServer(),
                        fileEntity.getFileName(),
                        "application/x-msdownload" ) ;
                return ;
            }
        }
        response.getWriter().write( "<script type=\"text/javascript\">alert('文件不存在！');</script>" ) ;
    }
    
    @RequestMapping( params = "command=downloadFile" )
    public void downloadFile( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
    	String filePathInServer = request.getParameter( "filePathInServer" ) ;
    	String fileName = request.getParameter( "fileName" ) ;
    	
    	if(StringUtils.isEmpty(filePathInServer)){
    		filePathInServer = (String) request.getAttribute("filePathInServer");
    	}
    	if(StringUtils.isEmpty(fileName)){
    		fileName = (String) request.getAttribute("fileName");
    	}
    	
    	filePathInServer=new String( filePathInServer.getBytes( "iso-8859-1" ),"UTF-8") ;
    	fileName=new String( fileName.getBytes( "iso-8859-1" ),"UTF-8") ;
    	File uploadFolder = new File(filePathInServer);
        if (!uploadFolder.exists()) 
        	response.getWriter().write( "<script type=\"text/javascript\">alert('文件不存在！');</script>" ) ;
        else
        	UploadHelper.sendFile(response, filePathInServer, fileName, "application/x-msdownload") ;
                
       
    }
    
    @RequestMapping( params = "command=downloadZip" )
    public void downloadZip( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        String idParam = request.getParameter( "ids" ) ;
        String[] ids = idParam.split( "," ) ;
        if( ids != null && ids.length > 0 )
        {
            StringBuffer zipFilePath = new StringBuffer() ; 
            String zipFileName = UUID.randomUUID().toString() + ".zip" ;
            zipFilePath.append( uploadPath ).append( zipFileName ) ;
            File zipFile = new File( zipFilePath.toString() ) ;
            if ( !zipFile.exists() )
            {   
                zipFile.createNewFile();   
            }
            List<EntityPlatformAccessory> fileEntityList = accessoryService.searchAccessoryByIdList( Arrays.asList( ids ) ) ;
            List<File> fileList = new ArrayList<File>() ;
            for( EntityPlatformAccessory entity : fileEntityList )
            {
                File file = new File( entity.getFilePathInServer() ) ;
                fileList.add( file ) ;
            }
            UploadHelper.zipFile( fileList, zipFile ) ;
            UploadHelper.sendFile(
                    response,
                    zipFilePath.toString(),
                    zipFileName,
                    "application/x-msdownload" ) ;
            zipFile.delete() ;
        }
        else
        {
            response.getWriter().write( "<script type=\"text/javascript\">alert('文件不存在！');</script>" ) ;
        }
    }

    /**
     * 删除指定数据库记录并且删除文件
     * 上传空间默认删除方法
     * @param id
     * @param out
     */
    @RequestMapping( params = "command=remove" )
    public void remove( @RequestParam( "id" )String id, java.io.PrintWriter out ) {
    	EntityPlatformAccessory entity = accessoryService.load( id ) ;
        accessoryService.remove( id ) ;
        File file = new File( entity.getFilePathInServer() ) ;
        if( file.exists() ){
            file.delete() ;
        }
        out.print("success");
        
    }
    
    /**
     * 删除指定数据库文件记录(不删除文件)
     * @param id
     * @param out
     */
    @RequestMapping( params = "command=removeWithFile" )
    public void removeWithFile( @RequestParam( "id" )String id, java.io.PrintWriter out ) {
        
        accessoryService.remove( id ) ;
        out.print("success");
        
    }
    
    @RequestMapping( params = "command=loadFileById" )
    public void loadFileById( @RequestParam( "id" ) String id, java.io.PrintWriter out ){
        EntityPlatformAccessory entity = accessoryService.load( id ) ;
        if(null !=entity){
            out.print(this.getSerializer().formatObject(entity));
        }
    }

    @SuppressWarnings("unchecked")
	@RequestMapping( params = "command=loadFileByIdsArr" )
    public void loadFileByIdsArr( @RequestParam( "data" ) String data, java.io.PrintWriter out ){
        DataSerializer serializer1= this.getSerializer();
        Map<String, Object> map=serializer1.parseMap(data);
        if(null !=map && !map.isEmpty()){
           List<String> listids= (List<String>) map.get("id");
            if(listids !=null && !listids.isEmpty()){
                List<EntityPlatformAccessory> filelist=accessoryService.searchAccessoryByIdList(listids);
                out.print(serializer1.formatList(filelist));
            }
        }
    }

    
    @SuppressWarnings({ "resource", "unused" })
	@RequestMapping( params = "command=uploadMethod" )
    public void uploadMethod( @RequestParam("xlsFile")CommonsMultipartFile xlsFile,java.io.PrintWriter out,HttpServletRequest request, HttpServletResponse response ) throws Exception {
//    	 File fullFile = new File(uploadPath+File.separator+xlsFile.getFileItem().getName());
		 String fileName = xlsFile.getFileItem().getName();
		   Calendar today = Calendar.getInstance();
	        String year = String.valueOf(today.get(Calendar.YEAR));
	        String month = String.valueOf(today.get(Calendar.MONTH) + 1);
	        String upPath = uploadPath + File.separator + year + File.separator + month + File.separator;
	        File uploadFolder = new File(upPath);
	        if (!uploadFolder.exists()) {
	            uploadFolder.mkdirs();
	        }
	        String message = "";
	        DiskFileItemFactory factory = new DiskFileItemFactory();
	        ServletFileUpload upload = new ServletFileUpload(factory);
	        upload.setHeaderEncoding("UTF-8"); 

             List<FileItem> list = upload.parseRequest(request);
             for(FileItem item : list){
                 //如果fileitem中封装的是普通输入项的数据
                 if(item.isFormField()){
                     String name = item.getFieldName();
                     //解决普通输入项的数据的中文乱码问题
                     String value = item.getString("UTF-8");
                     //value = new String(value.getBytes("iso8859-1"),"UTF-8");
                     System.out.println(name + "=" + value);
                 }else{//如果fileitem中封装的是上传文件
                     //得到上传的文件名称，
                     String filename = item.getName();
                     System.out.println(filename);
                     if(filename==null || filename.trim().equals("")){
                         continue;
                     }
                   //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
                    //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
                     filename = filename.substring(filename.lastIndexOf("\\")+1);
                     //获取item中的上传文件的输入流
                     InputStream in = item.getInputStream();
                     //创建一个文件输出流
                     FileOutputStream fileOut = new FileOutputStream(upPath + "\\" + filename);
                     //创建一个缓冲区
                     byte buffer[] = new byte[1024];
                     //判断输入流中的数据是否已经读完的标识
                     int len = 0;
                     //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
                     while((len=in.read(buffer))>0){
                         //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
                    	 fileOut.write(buffer, 0, len);
                     }
                     //关闭输入流
                     in.close();
                    //关闭输出流
                     out.close();
                     //删除处理文件上传时生成的临时文件
                     item.delete();
                     message = "文件上传成功！";
                 }
             }
/*	    File fullFiles= new File(fileName);
         String realName = fullFiles.getName();
         String realExtName = FileHelper.getExtFromFileName(realName);
         String serverName = UploadHelper.createFileName(request, realExtName);
         String serverPath = upPath + serverName;
         File savedFile = new File(serverPath);*/
    }
    	
    	

    @Autowired
    @Value( "#{implementsMap['dataSerializer']}" )
    private DataSerializer serializer ;
    @Autowired
    private PlatformAccessoryService accessoryService ;
    
    private static String uploadPath = UploadHelper.getRealFileUploadPath();
    private static String charSetName = ConfigContext.getStringSection( "framework.web.charset" ) ;
    
}