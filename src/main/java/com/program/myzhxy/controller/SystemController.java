package com.program.myzhxy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.program.myzhxy.entity.Admin;
import com.program.myzhxy.entity.LoginForm;
import com.program.myzhxy.entity.Student;
import com.program.myzhxy.entity.Teacher;
import com.program.myzhxy.service.AdminService;
import com.program.myzhxy.service.StudentService;
import com.program.myzhxy.service.TeacherService;
import com.program.myzhxy.util.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/*
放一些公共功能的，非CRUD操作的控制层代码
例如：获取验证码和校验验证码
*/

@RestController
@RequestMapping("sms/system")
public class SystemController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    /**
     *修改密码的Controller
     * POST /sms/system/updatePwd/123456/admin
     *      /sms/system/updatePwd/{oldPwd}/{newPwd}
     *      请求参数:
     *          oldpwd
     *          newPwd
     *          token 头
     *      响应的数据:
     *          Result OK data= null
     */
    @ApiOperation("Update User Pwd")
    @PostMapping("updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @ApiParam("token")@RequestHeader("token") String token,
            @ApiParam("oldPwd")@PathVariable("oldPwd") String oldPwd,
            @ApiParam("newPwd")@PathVariable("newPwd") String newPwd
    ){
        //1.检查token是否过期
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            //token过期
            return Result.fail().message("token expired,please login again!");
        }

        //2.获取用户ID类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        oldPwd = MD5.encrypt(oldPwd);
        newPwd = MD5.encrypt(newPwd);

        switch (userType){
            case 1:
                QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
                adminQueryWrapper.eq("id",userId.intValue());
                adminQueryWrapper.eq("password",oldPwd);
                Admin admin = adminService.getOne(adminQueryWrapper);
                if (admin != null) {
                    //说明原密码正确，修改密码
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                }else {
                    return Result.fail().message("The old password is wrong！");
                }
                break;

            case 2:
                QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
                studentQueryWrapper.eq("id",userId.intValue());
                studentQueryWrapper.eq("password",oldPwd);
                Student student = studentService.getOne(studentQueryWrapper);
                if (student != null) {
                    //说明原密码正确，修改密码
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                }else {
                    return Result.fail().message("The old password is wrong！");
                }
                break;

            case 3:
                QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
                teacherQueryWrapper.eq("id",userId.intValue());
                teacherQueryWrapper.eq("password",oldPwd);
                Teacher teacher = teacherService.getOne(teacherQueryWrapper);
                if (teacher != null) {
                    //说明原密码正确，修改密码
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                }else {
                    return Result.fail().message("The old password is wrong！");
                }
                break;
        }
        return Result.ok();
    }


    /**
     * 上传图片的方法
     * @param multipartFile
     * @return
     * @RequestPart 将图片文件抽象到NultipartFile对象，用来进行接收
     * @ApiOperation 文件上传统一入口
     */
    @ApiOperation("Common FileUpload Entrance")
    @PostMapping("headerImgUpload")
    public Result headerImgUpload(
            @ApiParam("avatarFile") @RequestPart("multipartFile") MultipartFile multipartFile,
            HttpServletRequest request
    ){
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String originalFilename = multipartFile.getOriginalFilename();
        int i = originalFilename.lastIndexOf(".");
        String newFileName = uuid + originalFilename.substring(i);

        /*1.保存文件:将文件发送到第三方独立的图片服务器
            现在先写死*/
        String portraitPath = "D:/JavaProgramme05/myzhxy/target/classes/static/images/" + newFileName;
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //2.响应图片路径
        String path = "images/" + newFileName;
        return Result.ok(path);
    }

    /**
     * 获取登录信息的方法
     * @param token
     * @return
     */
    @GetMapping("/getInfo")
    public Result getInfoByToken(@RequestHeader("token") String token){
        //1.验证token是否过期
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration){
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }

        //2.从token中解析出 用户id 和 用户类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        //3.判断用户类型
        Map<String,Object> map = new LinkedHashMap<>();
        switch (userType){
            case 1:
                Admin admin = adminService.getAdminById(userId);
                map.put("userType",1);
                map.put("user",admin);
                break;
            case 2:
                Student student = studentService.getStudentById(userId);
                map.put("userType",2);
                map.put("user",student);
                break;
            case 3:
                Teacher teacher = teacherService.getTeacherById(userId);
                map.put("userType",3);
                map.put("user",teacher);
                break;
        }
        return Result.ok(map);
    }

    /**
     为什么该方法的返回值Result?
        方法响应的结果应该是  code:200
                           message:"成功"
                           data:Object{ token:
                                ”密文“}
                           ok:true
        只要是以上格式，后端响应一定是一个 Result对象 转成 json格式的对象

     请求的参数：一个json格式的请求体，里面包含username，password，verifiCode，userType，
     每次都写四个参数比较麻烦，所以封装一个LoginForm的entity

     LoginForm如果直接写在变量中是不会自动进行转换为json格式的（前端是以请求体提交json字符串的形式提交）
     想要成功转换需要 @RequestBody
     */
    @PostMapping("login")
    public Result Login(@RequestBody LoginForm loginForm,
                        HttpServletRequest request){
        //1.验证码校验：一个验证码在LoginForm中，另一个在session中
        HttpSession session = request.getSession();
        String sessionVerifiCode = (String)session.getAttribute("verifiCode");
        String loginFormVerifiCode = loginForm.getVerifiCode();

        if ("".equals(sessionVerifiCode) || null == sessionVerifiCode){
            return Result.fail().message("验证码已失效，请刷新后重试!");
        }
        if (!sessionVerifiCode.equalsIgnoreCase(loginFormVerifiCode)){
            return Result.fail().message("验证码错误，请重新输入!");
        }
        //由于已经验证过一次验证码，就应该从session中移除现有验证码
        session.removeAttribute("verifiCode");

        //2.用户类型的校验：管理员、老师、学生需要查询的表不同
        /*
        使用JwtHelp生成token口令后，
        需要响应给浏览器(token放在data属性中，token这个密文字符串以token的名称放到了Result对象的data中)
        所以现在需要准备一个Map存放响应数据
        */
        Map<String,Object> map = new LinkedHashMap<>();
        switch (loginForm.getUserType()){
            case 1:
                try {
                    Admin admin = adminService.login(loginForm);
                    if(null != admin){
                        //用户的类型和id转换成密文，以token的名称向客户端反馈
                        String token = JwtHelper.createToken(admin.getId().longValue(), 1);
                        map.put("token",token);
                    }else { //如果没有找到，手动抛出Exception
                        throw new RuntimeException("用户名或者密码有误!");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());

                }
            case 2:
                try {
                    Student student = studentService.login(loginForm);
                    if(null != student){
                        //用户的类型和id转换成密文，以token的名称向客户端反馈
                        String token = JwtHelper.createToken(student.getId().longValue(), 2);
                        map.put("token",token);
                    }else { //如果没有找到，手动抛出Exception
                        throw new RuntimeException("用户名或者密码有误!");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 3:
                try {
                    Teacher teacher = teacherService.login(loginForm);
                    if(null != teacher){
                        //用户的类型和id转换成密文，以token的名称向客户端反馈
                        String token = JwtHelper.createToken(teacher.getId().longValue(), 3);
                        map.put("token",token);
                    }else { //如果没有找到，手动抛出Exception
                        throw new RuntimeException("用户名或者密码有误!");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }
        return Result.fail().message("未找到该用户!");
    }

    /**
     * 获取验证码的方法
     * @param request 将验证码文本放入session域
     * @param response 将验证码图片响应给浏览器
     */
    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request,
                                   HttpServletResponse response){
        //1.获取图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();

        //2.获取图片上的验证码
        String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());

        //3.将验证码文本放入session域，为下一次验证做准备
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode",verifiCode);

        //4.将验证码图片响应给浏览器
        try {
            ImageIO.write(verifiCodeImage,"JPEG",response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
