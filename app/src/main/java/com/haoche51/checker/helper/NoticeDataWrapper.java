package com.haoche51.checker.helper;

import com.haoche51.checker.DAO.NoticeDAO;
import com.haoche51.checker.entity.NoticeEntity;
import com.haoche51.checker.util.JsonParseUtil;

import java.util.List;

public class NoticeDataWrapper {
	
	/**
	 * 更新全部公告
	 * @param result
	 */
	public static void updateNotices (String result){
//		List<NoticeEntity> noticeList = new HCJsonParse().parseNoticeResult(result);
		List<NoticeEntity> noticeList = JsonParseUtil.fromJsonArray(result, NoticeEntity.class);
		if (noticeList != null) {
			for (NoticeEntity notice : noticeList) {
				NoticeEntity noticeEntity = (NoticeEntity) NoticeDAO.getInstance().get(notice.getId());
				if (noticeEntity == null) {
					NoticeDAO.getInstance().insert(notice);
				}
			}
		}
	}
	/**
	 * 读取本地全部线索
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<NoticeEntity> getAllNotices (){
		return (List<NoticeEntity>)NoticeDAO.getInstance().get();
	}
	
	
	/**
	 * 更新公告为已读状态
	 * @param noticeEntity
	 */
	public static void updateNoticeIsRead(NoticeEntity noticeEntity){
		noticeEntity.setIs_read(0);
		NoticeDAO.getInstance().update(noticeEntity.getId(), noticeEntity);
	}
	
}
