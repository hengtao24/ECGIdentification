package com.bme.ecgidentification.BmobTest;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bme.ecgidentification.R;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 *
 * @ClassName: MainActivity
 * @Description: TODO
 * @author smile
 * @date 2014-5-20 4:10:29
 */
public class MainActivity extends BaseActivity implements OnClickListener {

	Button btn_add, btn_delete, btn_update, btn_query;

	private String objectId="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);
		initView();
		initListener();
	}

	private void initView() {
		btn_add = (Button) findViewById(R.id.btn_add);
		btn_delete = (Button) findViewById(R.id.btn_delete);
		btn_update = (Button) findViewById(R.id.btn_update);
		btn_query = (Button) findViewById(R.id.btn_query);
	}

	private void initListener() {
		btn_add.setOnClickListener(this);
		btn_delete.setOnClickListener(this);
		btn_update.setOnClickListener(this);
		btn_query.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btn_add) {
			createPerson();
		} else if (v == btn_delete) {
			deletePersonByObjectId();
		} else if (v == btn_update) {
			updatePersonByObjectId();
		} else if (v == btn_query) {
			queryPersonByObjectId();
		}
	}

	/**
	 * 
	 * @Title: createPersonData
	 * @throws
	 */
	private void createPerson() {
		final Person p2 = new Person();
		p2.setName("lucky");
		p2.setAddress("1111");
		p2.save(new SaveListener<String>() {
			@Override
			public void done(String Id,BmobException e) {
				if(e == null){
					ShowToast("添加数据成功，返回objectId为："+Id);
					objectId = Id;
				}else{
					ShowToast("创建数据失败：" + e.getMessage());
				}
			}
		});
	}

	/**
	 * 
	 * @return void
	 * @throws
	 */
	private void updatePersonByObjectId() {
		final Person p2 = new Person();
		p2.setAddress("22222222");
		p2.update("c373854bdd", new UpdateListener() {
			@Override
			public void done(BmobException e) {
				if(e==null){
					ShowToast("更新成功:"+p2.getUpdatedAt());
				}else{
					ShowToast("更新失败：" + e.getMessage());
				}
			}

		});
	}

	/**
	 *
	 * @Title: deletePersonByObjectId
	 * @return void
	 * @throws
	 */
	private void deletePersonByObjectId() {
		final Person p2 = new Person();
		p2.setObjectId(objectId);
		p2.delete(new UpdateListener() {

			@Override
			public void done(BmobException e) {
				if(e==null){
					ShowToast("删除成功:"+p2.getUpdatedAt());
				}else{
					ShowToast("删除失败：" + e.getMessage());
				}
			}

		});
	}

	/**
	  * queryPerson
	  * @Title: queryPerson
	  * @throws
	  */
	private void queryPersonByObjectId() {
		BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
		bmobQuery.getObject("c373854bdd", new QueryListener<Person>() {
			@Override
			public void done(Person object,BmobException e) {
				if(e==null){
					ShowToast("查询成功");
				}else{
					ShowToast("查询失败：" + e.getMessage());
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
