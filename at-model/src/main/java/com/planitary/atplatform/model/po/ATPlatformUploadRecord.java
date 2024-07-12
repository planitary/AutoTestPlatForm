package com.planitary.atplatform.model.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * Excel上传记录表
 */
@Data
public class ATPlatformUploadRecord {

    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 上传记录id，唯一标识一个上传记录
     */
    private String recordId;

    /**
     * excel文件导入来源（批量新增接口、批量新增项目等）
     */
    private String fileSource;

    /**
     * 文件大小
     */
    private String fileSize;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件MD5
     */
    private String fileMD5;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 导入时间
     */
    @TableField(fill = FieldFill.INSERT)
    private String createTime;



}
