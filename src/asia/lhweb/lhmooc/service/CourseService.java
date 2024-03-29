package asia.lhweb.lhmooc.service;

import asia.lhweb.lhmooc.common.Result;
import asia.lhweb.lhmooc.model.Page;
import asia.lhweb.lhmooc.model.bean.Course;
import asia.lhweb.lhmooc.model.vo.CourseVo;

import java.util.List;

/**
 * 课程服务
 *
 * @author Administrator
 * @description 针对表【course(课程表)】的数据库操作Service
 * @createDate 2024-03-11 21:23:21
 * @date 2024/03/11
 */
public interface CourseService {

    /**
     * 获得排名top8课程
     *
     * @param sort 排序方式
     * @return {@link List}<{@link Course}>
     */
    List<CourseVo> getSortCoursesTop8(String sort);


    /**
     * 按类别获取
     *
     * @param course 课程
     * @return {@link List}<{@link Course}>
     */
    List<Course> getByCategory(Course course);

    /**
     * 添加
     *
     * @param course 课程
     * @return boolean
     */
    boolean add(Course course);


    Page<CourseVo> pageAndByCategory(int courseCategoryId, int pageNo, int pageSize, String sortType);

    /**
     * 获取课程细节
     *
     * @param courseId 进程id
     * @param pageNo   页面没有
     * @param pageSize 页面大小
     * @return {@link Result}<{@link CourseVo}>
     */
    Result<CourseVo> getCourseDetail(int courseId, int pageNo, int pageSize);

    boolean realDelete(Course course);

    /**
     * 更新
     *
     * @param courseId   进程id
     * @param coursename coursename
     * @param profile    配置文件
     * @param price      价格
     * @return {@link Result}
     */
    Result update(int courseId, String coursename, String profile, String price);

    /**
     * 购买课程
     *
     * @param userId   用户id
     * @param CourseId 进程id
     * @return {@link Result}
     */
    Result buyCourse(int userId, int CourseId);
}
