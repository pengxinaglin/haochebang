package com.haoche51.checker.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;

import java.math.BigDecimal;

/**
 * @File:AlertDialogUtil.java
 * @Package:com.haoche51.checker.util
 * @desc:对话框Util
 * @author:zhuzuofei
 * @date:2015-4-30 下午4:09:25
 */
public class AlertDialogUtil {

	/**
	 * 创建一个标题、消息、确定、取消的对话框
	 */
	public static void showStandardTitleMessageDialog(final Activity context, String title, String msg, String cancelStr, String determineStr, final OnDismissListener onDismissListener) {
		if (context == null || context.isFinishing()) return;
		final Dialog dialog = new Dialog(context, R.style.shareDialog);
		View root = View.inflate(context, R.layout.dialog_standard, null);
		((TextView) root.findViewById(R.id.title)).setText(title);
		if (!TextUtils.isEmpty(msg))
			((TextView) root.findViewById(R.id.msg)).setText(msg);
		/** 取消 */
		Button cancle = (Button) root.findViewById(R.id.cancel);
		cancle.setText(cancelStr);
		cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		/** 确定 */
		Button determine = (Button) root.findViewById(R.id.determine);
		determine.setText(determineStr);
		determine.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//回调关闭
				if (onDismissListener != null) {
					onDismissListener.onDismiss(null);
				}
				dialog.dismiss();
			}
		});
		dialog.setContentView(root);
		setDialogSize(context, dialog, (float) 0.85, 0);
		dialog.show();
	}

	/**
	 * 创建标准对话框
	 *
	 * @param context
	 * @param message
	 * @param yesListener
	 */
	public static void createDialog(Activity context, String message, final OnClickYesListener yesListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setNegativeButton(GlobalData.resourceHelper.getString(R.string.cancel), null);
		builder.setPositiveButton(GlobalData.resourceHelper.getString(R.string.action_ok), new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (yesListener != null) {
					yesListener.onClickYes();
				}
			}
		});
		builder.show();
	}

	/**
	 * 弹出一个输入VIN码的对话框
	 */
	public static void createInputVinCodeDialog(Activity context, final OnDismissListener onDismissListener) {
		if (context == null || context.isFinishing()) return;
		final Dialog dialog = new Dialog(context, R.style.shareDialog);
		View root = View.inflate(context, R.layout.dialog_input_vincode, null);
		final EditText et_content = (EditText) root.findViewById(R.id.et_content);

		/** 取消 */
		Button cancle = (Button) root.findViewById(R.id.cancel);
		cancle.setText(GlobalData.resourceHelper.getString(R.string.cancel));
		cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onDismissListener.onDismiss(null);
				dialog.dismiss();
			}
		});

		/** 确定 */
		Button determine = (Button) root.findViewById(R.id.determine);
		determine.setText(GlobalData.resourceHelper.getString(R.string.action_ok));
		determine.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//VIN码必须是17位
				if (TextUtils.isEmpty(et_content.getText()) || et_content.getText().toString().trim().length() != 17) {
					et_content.setText(null);
					et_content.setHint("VIN码不能为空且必须是17位");
					return;
				}

				if (onDismissListener != null) {
					Bundle data = new Bundle();
					data.putString("vinCode", et_content.getText().toString());
					onDismissListener.onDismiss(data);
				}
				dialog.dismiss();
			}
		});

		dialog.setContentView(root);
		setDialogSize(context, dialog, (float) 0.85, 0);
		dialog.show();
	}

	/**
	 * 弹出一个修改VIN码的对话框
	 */
	public static void createModifyVinCodeDialog(final Activity context, final String vin, final boolean flag, final OnDismissListener onDismissListener) {
		if (context == null || context.isFinishing()) return;
		final Dialog dialog = new Dialog(context, R.style.shareDialog);
		View root = View.inflate(context, R.layout.dialog_modify_vincode, null);
		((TextView) root.findViewById(R.id.et_content)).setText("您输入的VIN码为：\n" + vin + "，\n输入后不可修改，请确定");
		/** 修改 */
		Button cancle = (Button) root.findViewById(R.id.cancel);
		cancle.setText(GlobalData.resourceHelper.getString(R.string.modify));
		cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!flag)
					createInputVinCodeDialog(context, onDismissListener);
				else {
					Bundle data = new Bundle();
					data.putBoolean("determine", false);
					onDismissListener.onDismiss(data);
				}
				dialog.dismiss();
			}
		});

		/** 确定 */
		Button determine = (Button) root.findViewById(R.id.determine);
		determine.setText(GlobalData.resourceHelper.getString(R.string.action_ok));
		determine.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (onDismissListener != null) {
					Bundle data = new Bundle();
					data.putString("vinCode", vin);
					data.putBoolean("determine", true);
					onDismissListener.onDismiss(data);
				}
				dialog.dismiss();
			}
		});

		dialog.setContentView(root);
		setDialogSize(context, dialog, (float) 0.85, 0);
		dialog.show();
	}

	/**
	 * 弹出一个修改VIN码的对话框
	 */
	public static void createNormalDialog(final Activity context, final String content, final String cancelContent, final String okContent, final boolean flag, final OnDismissListener onDismissListener) {
		if (context == null || context.isFinishing()) return;
		final Dialog dialog = new Dialog(context, R.style.shareDialog);
		View root = View.inflate(context, R.layout.dialog_modify_vincode, null);
		((TextView) root.findViewById(R.id.et_content)).setText(content);
		/** 修改 */
		Button cancle = (Button) root.findViewById(R.id.cancel);
		cancle.setText(cancelContent);
		cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!flag)
					createInputVinCodeDialog(context, onDismissListener);
				else {
					Bundle data = new Bundle();
					data.putBoolean("determine", false);
					onDismissListener.onDismiss(data);
				}
				dialog.dismiss();
			}
		});

		/** 确定 */
		Button determine = (Button) root.findViewById(R.id.determine);
		determine.setText(okContent);
		determine.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (onDismissListener != null) {
					Bundle data = new Bundle();
					data.putBoolean("determine", true);
					onDismissListener.onDismiss(data);
				}
				dialog.dismiss();
			}
		});

		dialog.setContentView(root);
		setDialogSize(context, dialog, (float) 0.85, 0);
		dialog.show();
	}

	/**
	 * 请求VIN码鉴定报告成功对话框
	 */
	public static void createRequestVinSuccessAutoDismissDialog(final Activity context) {
		if (context == null || context.isFinishing()) return;
		final Dialog dialog = new Dialog(context, R.style.shareDialog);
		View root = View.inflate(context, R.layout.dialog_vin_success, null);
		dialog.setContentView(root);
		setDialogSize(context, dialog, (float) 0.65, 0);
		setDialogBackground(dialog, R.drawable.shape_toast);
		dialog.show();

		root.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!context.isFinishing())
					dialog.dismiss();
			}
		}, 2000);
	}

	/**
	 * 未检索到VIN报告对话框
	 */
	public static void createNotFoundReportDialog(final Activity context, String vin, final OnDismissListener onDismissListener) {
		if (context == null || context.isFinishing()) return;
		final Dialog dialog = new Dialog(context, R.style.shareDialog);
		View root = View.inflate(context, R.layout.dialog_notfound_report, null);
		((TextView) root.findViewById(R.id.et_content)).setText("您输入的VIN码为：\n" + (TextUtils.isEmpty(vin) ? "" : vin) + "，\n未检索到车鉴定报告，请确认是否\n输入正确，若正确则该车没有报\n告，若错误则点击修改");
		/** 修改 */
		Button cancel = (Button) root.findViewById(R.id.cancel);
		cancel.setText(GlobalData.resourceHelper.getString(R.string.modify));
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createInputVinCodeDialog(context, onDismissListener);
				dialog.dismiss();
			}
		});

		/** 确定 */
		Button determine = (Button) root.findViewById(R.id.determine);
		determine.setText(GlobalData.resourceHelper.getString(R.string.action_ok));
		determine.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});

		dialog.setContentView(root);
		setDialogSize(context, dialog, (float) 0.85, 0);
		dialog.show();
	}

	/**
	 * 弹出一个输入发动机编号的对话框
	 */
	public static void createInputEngineCodeDialog(Activity context, final OnDismissListener onDismissListener) {
		if (context == null || context.isFinishing()) return;
		final Dialog dialog = new Dialog(context, R.style.shareDialog);
		View root = View.inflate(context, R.layout.dialog_input_vincode, null);
		final EditText et_content = (EditText) root.findViewById(R.id.et_content);
		et_content.setHint(context.getString(R.string.input_engine_cod));

		/** 取消 */
		Button cancle = (Button) root.findViewById(R.id.cancel);
		cancle.setText(GlobalData.resourceHelper.getString(R.string.p_cancel));
		cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				onDismissListener.onDismiss(null);
				dialog.dismiss();
			}
		});

		/** 确定 */
		Button determine = (Button) root.findViewById(R.id.determine);
		determine.setText(GlobalData.resourceHelper.getString(R.string.action_ok));
		determine.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//发送机编号不能为空
				if (TextUtils.isEmpty(et_content.getText()) || et_content.getText().toString().trim().length()==0) {
					et_content.setText(null);
					et_content.setHint("发动机编号不能为空!");
					return;
				}

				if (onDismissListener != null) {
					Bundle data = new Bundle();
					data.putString("engine", et_content.getText().toString());
					onDismissListener.onDismiss(data);
				}
				dialog.dismiss();
			}
		});

		dialog.setContentView(root);
		setDialogSize(context, dialog, (float) 0.85, 0);
		dialog.show();
	}

	/**
	 * 弹出一个添加”我的备注“的对话框
	 */
	public static void createCheckerCommentDialog(final Activity context, boolean flag, final String comment, final OnDismissListener onDismissListener) {
		if (context == null || context.isFinishing()) return;
		final Dialog dialog = new Dialog(context, R.style.shareDialog);
		View root = View.inflate(context, R.layout.dialog_input_remark, null);
		TextView title = (TextView) root.findViewById(R.id.title);
		title.setText(flag ? GlobalData.resourceHelper.getString(R.string.add_remark) : GlobalData.resourceHelper.getString(R.string.modify_remark));
		final EditText et_content = (EditText) root.findViewById(R.id.et_content);
		if (!TextUtils.isEmpty(comment)) {
			et_content.setText(comment);
			et_content.setSelection(comment.length());
		}
		/** 取消 */
		Button cancle = (Button) root.findViewById(R.id.cancel);
		cancle.setText(GlobalData.resourceHelper.getString(R.string.cancel));
		cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		/** 确定 */
		Button determine = (Button) root.findViewById(R.id.determine);
		determine.setText(GlobalData.resourceHelper.getString(R.string.action_ok));
		determine.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TextUtils.isEmpty(et_content.getText().toString()) || et_content.getText().toString().length() > 50) {
					ToastUtil.showInfo(GlobalData.resourceHelper.getString(R.string.tip_input_remark));
					return;
				}

				//回传数据
				if (onDismissListener != null) {
					Bundle data = new Bundle();
					//防止故意输一大堆换行
					data.putString("comment", et_content.getText().toString().replace("\n", ""));
					onDismissListener.onDismiss(data);
				}
				dialog.dismiss();
			}
		});
		dialog.setContentView(root);
		setDialogSize(context, dialog, (float) 0.85, 0);
		dialog.show();
	}


	public static void setDialogSize(Activity context, Dialog dialog, float scaleW, float scaleH) {
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		if (scaleW > 0)
			lp.width = (int) (context.getWindowManager().getDefaultDisplay().getWidth() * scaleW);
		else
			lp.width = WindowManager.LayoutParams.WRAP_CONTENT;

		if (scaleH > 0)
			lp.height = (int) (context.getWindowManager().getDefaultDisplay().getHeight() * scaleH);
		else
			lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
	}

	private static void setDialogBackground(Dialog dialog, int res) {
		Window window = dialog.getWindow();
		window.setBackgroundDrawableResource(res);
	}

	/**
	 * 弹出一个”调整报价“的对话框
	 */
	public static void createModifyPriceDialog(final Activity context, float reportPrice, float lowPrice, final OnDismissListener onDismissListener) {
		if (context == null || context.isFinishing()) return;
		final Dialog dialog = new Dialog(context, R.style.shareDialog);
		View root = View.inflate(context, R.layout.dialog_modify_price, null);
		final EditText ed_offer_price = (EditText) root.findViewById(R.id.ed_offer_price);
		final EditText ed_low_price = (EditText) root.findViewById(R.id.ed_low_price);
		ed_offer_price.setText(String.valueOf(reportPrice));
		ed_offer_price.setSelection(String.valueOf(reportPrice).length());
		ed_low_price.setText(String.valueOf(lowPrice));
		ed_low_price.setSelection(String.valueOf(lowPrice).length());
		/** 取消 */
		Button cancle = (Button) root.findViewById(R.id.cancel);
		cancle.setText(GlobalData.resourceHelper.getString(R.string.cancel));
		cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		/** 确定 */
		Button determine = (Button) root.findViewById(R.id.determine);
		determine.setText(GlobalData.resourceHelper.getString(R.string.action_ok));
		determine.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TextUtils.isEmpty(ed_offer_price.getText())) {
					ToastUtil.showInfo("车辆报价不能为空");
					return;
				}

				if (TextUtils.isEmpty(ed_low_price.getText())) {
					ToastUtil.showInfo("车辆底价不能为空");
					return;
				}

				BigDecimal offerPrice = new BigDecimal(ed_offer_price.getText().toString());
				BigDecimal lowPrice = new BigDecimal(ed_low_price.getText().toString());
				if (offerPrice.floatValue() <= lowPrice.floatValue()) {
					ToastUtil.showInfo("车辆报价必须高于底价");
					return;
				}


				//回传数据
				if (onDismissListener != null) {
					Bundle data = new Bundle();
					data.putFloat("offer_price", offerPrice.floatValue());
					data.putFloat("low_price", lowPrice.floatValue());
					onDismissListener.onDismiss(data);
				}
				dialog.dismiss();
			}
		});
		dialog.setContentView(root);
		setDialogSize(context, dialog, (float) 0.85, 0);
		dialog.show();
	}

	/**
	 * 弹出一个提示的确定取消对话框
	 */
	public static void createDetermineCancelDialog(final Activity context, String msgText, String determineText, String cancelText, final OnDismissListener onDismissListener) {
		if (context == null || context.isFinishing()) return;
		final Dialog dialog = new Dialog(context, R.style.shareDialog);
		View root = View.inflate(context, R.layout.dialog_determine_and_cancel, null);
		/** 对话框内容 */
		TextView textView = (TextView) root.findViewById(R.id.tv_msg);
		textView.setText(msgText);
		/** 关闭 */
		root.findViewById(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//回传数据
				if (onDismissListener != null) {
					onDismissListener.onDismiss(null);
				}
				dialog.dismiss();
			}
		});
		/** 取消 */
		Button cancle = (Button) root.findViewById(R.id.cancel);
		cancle.setText(cancelText);
		cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//回传数据
				if (onDismissListener != null) {
					onDismissListener.onDismiss(null);
				}
				dialog.dismiss();
			}
		});
		/** 确定 */
		Button determine = (Button) root.findViewById(R.id.determine);
		determine.setText(determineText);
		determine.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//回传数据
				if (onDismissListener != null) {
					Bundle data = new Bundle();
					data.putBoolean("determine", true);
					onDismissListener.onDismiss(data);
				}
				dialog.dismiss();
			}
		});
		dialog.setContentView(root);
		setDialogSize(context, dialog, (float) 0.85, 0);
		dialog.show();
	}

	/**
	 * 确定手机号、金额
	 */
	public static void showConfirmPhoneMoneyDialog(final Activity context, String phone, int money, int transFee,
	                                               String leftButtonText, String rightButtonText, final OnDismissListener onDismissListener) {
		if (context == null || context.isFinishing()) {
			return;
		}

		final Dialog dialog = new Dialog(context, com.haoche51.settlement.R.style.pay_shareDialog);
		View view = View.inflate(context, com.haoche51.checker.R.layout.pay_dialog_confirm_phone_money, null);
		// 内容1
		TextView content1View = (TextView) view.findViewById(R.id.content1);
		content1View.setText(context.getString(R.string.again_confirm_phone));
		// 电话
		TextView phoneView = (TextView) view.findViewById(R.id.phone);
		phoneView.setText(phone);
		// 内容2
		TextView content2View = (TextView) view.findViewById(R.id.content2);
		content2View.setText(context.getString(R.string.again_confirm_price));
		// 金额
		TextView moneyView = (TextView) view.findViewById(R.id.money);
		moneyView.setText(money + "元");
		// 内容3
		TextView content3View = (TextView) view.findViewById(R.id.content3);
		content3View.setText(context.getString(R.string.again_confirm_trans_fee));
		content3View.setVisibility(transFee == 0 ? View.GONE : View.VISIBLE);
		// 过户费用
		TextView transFeeView = (TextView) view.findViewById(R.id.trans_fee);
		transFeeView.setText(transFee + "元");
		transFeeView.setVisibility(transFee == 0 ? View.GONE : View.VISIBLE);

		// 左按钮
		Button leftButton = (Button) view.findViewById(com.haoche51.checker.R.id.left_button);
		leftButton.setText(leftButtonText);
		leftButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		// 右按钮
		Button rightButton = (Button) view.findViewById(com.haoche51.checker.R.id.right_button);
		rightButton.setText(rightButtonText);
		rightButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (onDismissListener != null) {
					onDismissListener.onDismiss(null);
				}
				dialog.dismiss();
			}
		});

		dialog.setContentView(view);
		setDialogSize(context, dialog, (float) 0.85, 0);
		dialog.show();
	}

	//确定Listener
	public interface OnClickYesListener {
		 void onClickYes();
	}

	//确定Listener
	public interface OnDismissListener {
		 void onDismiss(Bundle data);
	}

	/**
	 * 弹出一个添加“下线原因”的对话框
	 */
	public static void createOfflineDialog(final Activity context, final OnDismissListener onDismissListener) {
		if (context == null || context.isFinishing()) return;
		final Dialog dialog = new Dialog(context, R.style.shareDialog);
		View root = View.inflate(context, R.layout.dialog_input_reason, null);
		TextView title = (TextView) root.findViewById(R.id.title);
		title.setText(GlobalData.resourceHelper.getString(R.string.hc_offline_vehicle));
		final EditText et_content = (EditText) root.findViewById(R.id.et_content);
		/** 取消 */
		Button cancle = (Button) root.findViewById(R.id.cancel);
		cancle.setText(GlobalData.resourceHelper.getString(R.string.cancel));
		cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		/** 确定 */
		Button determine = (Button) root.findViewById(R.id.determine);
		determine.setText(GlobalData.resourceHelper.getString(R.string.action_ok));
		determine.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TextUtils.isEmpty(et_content.getText().toString())) {
					ToastUtil.showInfo(GlobalData.resourceHelper.getString(R.string.tip_input_reason));
					return;
				}

				//回传数据
				if (onDismissListener != null) {
					Bundle data = new Bundle();
					//防止故意输一大堆换行
					data.putString("reason", et_content.getText().toString().replace("\n", ""));
					onDismissListener.onDismiss(data);
				}
				dialog.dismiss();
			}
		});
		dialog.setContentView(root);
		setDialogSize(context, dialog, (float) 0.85, 0);
		dialog.show();
	}

	/**
	 * 确定应退款金额和实际退款金额
	 */
	public static void showNeedBackAmtDialog(final Activity context, int needBackAmt, int realBackAmt,
											 String leftButtonText, String rightButtonText, final OnDismissListener onDismissListener) {
		if (context == null || context.isFinishing()) {
			return;
		}

		final Dialog dialog = new Dialog(context, com.haoche51.settlement.R.style.pay_shareDialog);
		View view = View.inflate(context, R.layout.dialog_confirm_back_money, null);
		// 应退金额
		TextView needBackTxt = (TextView) view.findViewById(R.id.need_back_txt);
		needBackTxt.setText(context.getString(R.string.again_confirm_need_back_amt));
		TextView needBackAmtView = (TextView) view.findViewById(R.id.need_back_amt);
		needBackAmtView.setText(String.valueOf(needBackAmt).concat("元"));

		// 实退金额
		TextView realBackTxt = (TextView) view.findViewById(R.id.real_back_txt);
		realBackTxt.setText(context.getString(R.string.again_confirm_real_back_amt));
		TextView realBackAmtView = (TextView) view.findViewById(R.id.real_back_amt);
		realBackAmtView.setText(String.valueOf(realBackAmt).concat("元"));
		// 左按钮
		Button leftButton = (Button) view.findViewById(com.haoche51.checker.R.id.left_button);
		leftButton.setText(leftButtonText);
		leftButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		// 右按钮
		Button rightButton = (Button) view.findViewById(com.haoche51.checker.R.id.right_button);
		rightButton.setText(rightButtonText);
		rightButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (onDismissListener != null) {
					onDismissListener.onDismiss(null);
				}
				dialog.dismiss();
			}
		});

		dialog.setContentView(view);
		setDialogSize(context, dialog, (float) 0.85, 0);
		dialog.show();
	}

}
