package com.haoche51.checker.constants;

public class PictureConstants {

	/** 外观图标识*/
	public static final int OUTER_PICTURE_LIMIT = 11;
	public static final String OUTER_PICTURE_TYPE = "outer_pics";
	public static final int OUTER_PICTURE_CHOSE = 0x123 ;
	/** 内饰图标识*/
	public static final int INNER_PICTURE_LIMIT = 16;
	public static final String INNER_PICTURE_TYPE = "inner_pics";
	public static final int INNER_PICTURE_CHOSE = 0x124;
	/** 细节图标识*/
	public static final String DETAIL_PICTURE_TYPE = "detail_pics";
	public static final int DETAIL_PICTURE_CHOSE = 0x125;
	/** 瑕疵图标识*/
	public static final int SINGLE_PICTURE_LIMIT = 1; //瑕疵每次只能选一张。需要标瑕疵位置
	public static final String DEFECT_PICTURE_TYPE = "defect_pics";
	public static final int DEFECT_PICTURE_CHOSE = 0x126;

  /** 选择汽车图片 */
  public static final int SELECT_CAR_PICTURE = 20;
  /** 选择瑕疵图片 */
  public static final int SELECT_DEFECT_PICTURE = 30;
	/** 选择视频 */
	public static final int SELECT_VIDEO = 40;
	/** 选择音频 */
	public static final int SELECT_AUDIO = 50;

  public static final int DELETESTATUS_DELTE = 1;//删除
  public static final int DELETESTATUS_FINISH = 2;//完成
  public static final int DELETESTATUS_CANCEL = 3;//取消
  public static final int DELETESTATUS_NULL = 4;//没有图片

	public static final int UPLOAD_IMAGE_SUCCESS = 0x201;//上传图片成功

	public static final int UPLOAD_UNKNOW_ERROR = 0x202; //未知异常

	public static final int UPLOAD_REPORT_SUCCESS = 0x203;

	public static final int UPLOAD_REPORT_FAILED = 0x204;

	public static final int GET_QINIU_TOKEN_SUCCESS = 0x206;

	public static final int GET_QINIU_TOKEN_FAILED = 0x207;

	/**
	 * 找不到图片
	 */
	public static final int PHOTO_NOT_FOUND = 0x208;
	/**
	 * 报告上传失败，请求响应结果为空
	 */
	public static final int UPLOAD_REPORT_FAILED_OTHER = 0x209;

	/**
	 * 当前上传照片线程尚未执行完
	 */
	public static final int UPLOAD_PHOTO_NOT_FINISH = 0x210;
	public static final int UPLOAD_IMAGE = 0x211;
	public static final int UPLOAD_COMPLETE = 0x212;

	public static final int QINIU_UPLOAD_SERVER_ERROR = 0x301;//QINIU 服务器异常
	public static final int QINIU_UPLOAD_RETURN_ERROR = 0x302;//QINIU 返回键值对为空
	public static final int UPLOAD_NETWORK_ERROR = 0x303; //网络上传错误


	/**
	 * 压缩视音频失败
	 */
	public static final int FAILED_COMPRESS_MEDIA = 0x306;

	/**
	 * 压缩视频完成
	 */
	public static final int FINISH_COMPRESS_VIDEO = 0x307;

	/**
	 * 更新任务进度
	 */
	public static final int UPDATE_TASK_PROGRESS = 0x309;

	/**
	 * 上传视频完成
	 */
	public static final int FINISH_UPLOAD_VIDEO = 0x310;

	/**
	 * 上传音频完成
	 */
	public static final int FINISH_UPLOAD_AUDIO = 0x311;


	/**
	 * 图片上传完成
	 */
	public static final int FINISH_UPLOAD_PHOTO = 0x312;

	/**
	 * 重写音视频URL出错
	 */
	public static final int UPLOAD_WRITE_MEDIA_URL_ERROR = 0x313;

	/**
	 * 上传类型：图片
	 */
	public static final int UPLOAD_TYPE_PHOTO = 1;

	/**
	 * 上传类型：视频
	 */
	public static final int UPLOAD_TYPE_VIDEO = 2;

	/**
	 * 上传类型：音频
	 */
	public static final int UPLOAD_TYPE_AUDIO = 3;


	/**
	 * 压缩图片
	 */
	public static final int COMPRESS_IMAGE_SUCCESS = 1;//压缩单张图片成功
	public static final int COMPRESS_IMAGE_FAILED = 2;//压缩单张图片失败
	public static final int COMPRESS_ALL_COMPLETE = 3;//压缩图片完成

	/**
	 * 结束视频复制
	 */
	public static final int FINISH_COPY_VIDEO = 4;

	/**
	 * 视频复制失败
	 */
	public static final int FAILED_COPY_VIDEO = 5;


}
