package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.entity.StudentInfo;
import generator.service.StudentInfoService;
import generator.mapper.StudentInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author CAIXYPROMISE
* @description 针对表【student_info(学生信息表)】的数据库操作Service实现
* @createDate 2025-04-07 00:24:01
*/
@Service
public class StudentInfoServiceImpl extends ServiceImpl<StudentInfoMapper, StudentInfo>
    implements StudentInfoService{

}




