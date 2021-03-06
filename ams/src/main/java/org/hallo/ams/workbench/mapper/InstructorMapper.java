package org.hallo.ams.workbench.mapper;

import org.hallo.ams.workbench.domain.Instructor;

import java.util.List;

public interface InstructorMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_instructor
     *
     * @mbg.generated Mon Apr 04 17:25:56 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_instructor
     *
     * @mbg.generated Mon Apr 04 17:25:56 CST 2022
     */
    int insert(Instructor record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_instructor
     *
     * @mbg.generated Mon Apr 04 17:25:56 CST 2022
     */
    int insertSelective(Instructor record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_instructor
     *
     * @mbg.generated Mon Apr 04 17:25:56 CST 2022
     */
    Instructor selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_instructor
     *
     * @mbg.generated Mon Apr 04 17:25:56 CST 2022
     */
    int updateByPrimaryKeySelective(Instructor record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_instructor
     *
     * @mbg.generated Mon Apr 04 17:25:56 CST 2022
     */
    int updateByPrimaryKey(Instructor record);


    /**
     * 查询所有教师名字
     * @param name
     * @return
     */
    List<String> selectInstructorNameByName(String name);





}