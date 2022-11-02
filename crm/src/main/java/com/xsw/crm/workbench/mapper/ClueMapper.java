package com.xsw.crm.workbench.mapper;

import com.xsw.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Mon Oct 17 11:08:19 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Mon Oct 17 11:08:19 CST 2022
     */
    int insert(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Mon Oct 17 11:08:19 CST 2022
     */
    int insertSelective(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Mon Oct 17 11:08:19 CST 2022
     */
    Clue selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Mon Oct 17 11:08:19 CST 2022
     */
    int updateByPrimaryKeySelective(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Mon Oct 17 11:08:19 CST 2022
     */
    int updateByPrimaryKey(Clue record);

    /**
     * 保存创建的线索
     * @param clue
     * @return
     */
    int insertClue(Clue clue);

    /**
     * 根据条件分页查询线索的列表
     * @param map
     * @return
     */
    List<Clue> selectClueByConditionForPage(Map<String,Object> map);

    /**
     * 根据条件查询线索的总条数
     * @param map
     * @return
     */
    int selectCountOfClueByCondition(Map<String,Object> map);

    /**
     * 根据线索id查询线索明细信息
     * @param id
     * @return
     */
    Clue selectClueForDetailById(String id);

    /**
     * 根据id查询线索的信息
     * @param id
     * @return
     */
    Clue selectClueById(String id);

    /**
     * 根据id删除线索
     * @param clueId
     * @return
     */
    int deleteClueById(String clueId);
}
