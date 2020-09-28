package app.gif.mzanzify.premium;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.*;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.util.*;
import java.text.*;
import java.util.HashMap;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Switch;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnProgressListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Continuation;
import android.net.Uri;
import java.io.File;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import android.content.Intent;
import android.content.ClipData;
import android.view.View;
import android.widget.CompoundButton;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class UploadActivity extends AppCompatActivity {
	
	public final int REQ_CD_PICKER = 101;
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	private FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();
	
	private String path = "";
	private String name = "";
	private HashMap<String, Object> upload_map = new HashMap<>();
	private boolean isExitDisabled = false;
	
	private LinearLayout linear1;
	private LinearLayout linear2;
	private ImageView image_back;
	private TextView textview_title;
	private LinearLayout agrg;
	private LinearLayout linear9;
	private LinearLayout linzz;
	private LinearLayout linback;
	private LinearLayout linear5;
	private TextView textview3;
	private Button button1;
	private TextView textview2;
	private Switch switch2;
	private TextView textview1;
	private Switch switch1;
	private LinearLayout linear3;
	private LinearLayout linear4;
	private EditText edittext_name;
	private Button button_upload;
	private TextView textview_path;
	private Button button_browse;
	private ProgressBar progressbar1;
	private LinearLayout linear6;
	private TextView textview_upload;
	private TextView textview_prog;
	
	private StorageReference upload_storage = _firebase_storage.getReference("upload/music");
	private OnCompleteListener<Uri> _upload_storage_upload_success_listener;
	private OnSuccessListener<FileDownloadTask.TaskSnapshot> _upload_storage_download_success_listener;
	private OnSuccessListener _upload_storage_delete_success_listener;
	private OnProgressListener _upload_storage_upload_progress_listener;
	private OnProgressListener _upload_storage_download_progress_listener;
	private OnFailureListener _upload_storage_failure_listener;
	private DatabaseReference upload_text = _firebase.getReference("upload/text");
	private ChildEventListener _upload_text_child_listener;
	private FirebaseAuth Auth;
	private OnCompleteListener<Void> Auth_updateEmailListener;
	private OnCompleteListener<Void> Auth_updatePasswordListener;
	private OnCompleteListener<Void> Auth_emailVerificationSentListener;
	private OnCompleteListener<Void> Auth_deleteUserListener;
	private OnCompleteListener<Void> Auth_updateProfileListener;
	private OnCompleteListener<AuthResult> Auth_phoneAuthListener;
	private OnCompleteListener<AuthResult> Auth_googleSignInListener;
	private OnCompleteListener<AuthResult> _Auth_create_user_listener;
	private OnCompleteListener<AuthResult> _Auth_sign_in_listener;
	private OnCompleteListener<Void> _Auth_reset_password_listener;
	private Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.upload);
		com.google.firebase.FirebaseApp.initializeApp(this);
		initialize(_savedInstanceState);
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
		}
		else {
			initializeLogic();
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		image_back = (ImageView) findViewById(R.id.image_back);
		textview_title = (TextView) findViewById(R.id.textview_title);
		agrg = (LinearLayout) findViewById(R.id.agrg);
		linear9 = (LinearLayout) findViewById(R.id.linear9);
		linzz = (LinearLayout) findViewById(R.id.linzz);
		linback = (LinearLayout) findViewById(R.id.linback);
		linear5 = (LinearLayout) findViewById(R.id.linear5);
		textview3 = (TextView) findViewById(R.id.textview3);
		button1 = (Button) findViewById(R.id.button1);
		textview2 = (TextView) findViewById(R.id.textview2);
		switch2 = (Switch) findViewById(R.id.switch2);
		textview1 = (TextView) findViewById(R.id.textview1);
		switch1 = (Switch) findViewById(R.id.switch1);
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		linear4 = (LinearLayout) findViewById(R.id.linear4);
		edittext_name = (EditText) findViewById(R.id.edittext_name);
		button_upload = (Button) findViewById(R.id.button_upload);
		textview_path = (TextView) findViewById(R.id.textview_path);
		button_browse = (Button) findViewById(R.id.button_browse);
		progressbar1 = (ProgressBar) findViewById(R.id.progressbar1);
		linear6 = (LinearLayout) findViewById(R.id.linear6);
		textview_upload = (TextView) findViewById(R.id.textview_upload);
		textview_prog = (TextView) findViewById(R.id.textview_prog);
		Auth = FirebaseAuth.getInstance();
		picker.setType("audio/*");
		picker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		
		image_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (!isExitDisabled) {
					finish();
				}
			}
		});
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				agrg.setVisibility(View.GONE);
				linback.setVisibility(View.VISIBLE);
			}
		});
		
		switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton _param1, boolean _param2)  {
				final boolean _isChecked = _param2;
				linear9.setVisibility(View.GONE);
				linzz.setVisibility(View.VISIBLE);
			}
		});
		
		switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton _param1, boolean _param2)  {
				final boolean _isChecked = _param2;
				linzz.setVisibility(View.GONE);
				agrg.setVisibility(View.VISIBLE);
			}
		});
		
		button_upload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (20 > (FileUtil.getFileLength(path) / 1000000)) {
					name = edittext_name.getText().toString();
					upload_storage.child(name.concat(Uri.parse(path).getLastPathSegment().replace(Uri.parse(path).getLastPathSegment().substring((int)(0), (int)(Uri.parse(path).getLastPathSegment().lastIndexOf("."))), ""))).putFile(Uri.fromFile(new File(path))).addOnFailureListener(_upload_storage_failure_listener).addOnProgressListener(_upload_storage_upload_progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
						@Override
						public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
							return upload_storage.child(name.concat(Uri.parse(path).getLastPathSegment().replace(Uri.parse(path).getLastPathSegment().substring((int)(0), (int)(Uri.parse(path).getLastPathSegment().lastIndexOf("."))), ""))).getDownloadUrl();
						}}).addOnCompleteListener(_upload_storage_upload_success_listener);
					linear3.setVisibility(View.GONE);
					linear5.setVisibility(View.VISIBLE);
					isExitDisabled = true;
				}
				else {
					QueryUtil.showMessage(getApplicationContext(), "Max upload file size  ≤ 20 MB");
				}
			}
		});
		
		button_browse.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				startActivityForResult(picker, REQ_CD_PICKER);
			}
		});
		
		_upload_storage_upload_progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onProgress(UploadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				textview_prog.setText(String.valueOf((long)(_progressValue)).concat("% Uploaded"));
			}
		};
		
		_upload_storage_download_progress_listener = new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onProgress(FileDownloadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				
			}
		};
		
		_upload_storage_upload_success_listener = new OnCompleteListener<Uri>() {
			@Override
			public void onComplete(Task<Uri> _param1) {
				final String _downloadUrl = _param1.getResult().toString();
				upload_map = new HashMap<>();
				upload_map.put("url", _downloadUrl);
				upload_map.put("name", name);
				upload_map.put("pub", FirebaseAuth.getInstance().getCurrentUser().getEmail());
				upload_text.push().updateChildren(upload_map);
				QueryUtil.showMessage(getApplicationContext(), "Upload Completed!");
				finish();
			}
		};
		
		_upload_storage_download_success_listener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(FileDownloadTask.TaskSnapshot _param1) {
				final long _totalByteCount = _param1.getTotalByteCount();
				
			}
		};
		
		_upload_storage_delete_success_listener = new OnSuccessListener() {
			@Override
			public void onSuccess(Object _param1) {
				
			}
		};
		
		_upload_storage_failure_listener = new OnFailureListener() {
			@Override
			public void onFailure(Exception _param1) {
				final String _message = _param1.getMessage();
				QueryUtil.showMessage(getApplicationContext(), _message);
				finish();
			}
		};
		
		_upload_text_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onChildMoved(DataSnapshot _param1, String _param2) {
				
			}
			
			@Override
			public void onChildRemoved(DataSnapshot _param1) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onCancelled(DatabaseError _param1) {
				final int _errorCode = _param1.getCode();
				final String _errorMessage = _param1.getMessage();
				
			}
		};
		upload_text.addChildEventListener(_upload_text_child_listener);
		
		Auth_updateEmailListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		Auth_updatePasswordListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		Auth_emailVerificationSentListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		Auth_deleteUserListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		Auth_phoneAuthListener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> task){
				final boolean _success = task.isSuccessful();
				final String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";
				
			}
		};
		
		Auth_updateProfileListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		Auth_googleSignInListener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> task){
				final boolean _success = task.isSuccessful();
				final String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";
				
			}
		};
		
		_Auth_create_user_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		_Auth_sign_in_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		_Auth_reset_password_listener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				
			}
		};
	}
	private void initializeLogic() {
		
		linback.setVisibility(View.GONE);
		linear5.setVisibility(View.GONE);
		linzz.setVisibility(View.GONE);
		agrg.setVisibility(View.GONE);
		textview_title.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/googlesans.ttf"), 1);
		edittext_name.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/googlesans.ttf"), 0);
		button_upload.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/googlesans.ttf"), 1);
		textview_path.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/googlesans.ttf"), 0);
		button_browse.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/googlesans.ttf"), 1);
		textview_upload.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/googlesans.ttf"), 1);
		textview_prog.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/googlesans.ttf"), 0);
		textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/googlesans.ttf"), 0);
		switch1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/googlesans.ttf"), 0);
		textview2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/googlesans.ttf"), 0);
		switch2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/googlesans.ttf"), 0);
		textview3.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/googlesans.ttf"), 0);
		button1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/googlesans.ttf"), 0);
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_PICKER:
			if (_resultCode == Activity.RESULT_OK) {
				ArrayList<String> _filePath = new ArrayList<>();
				if (_data != null) {
					if (_data.getClipData() != null) {
						for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
							ClipData.Item _item = _data.getClipData().getItemAt(_index);
							_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _item.getUri()));
						}
					}
					else {
						_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _data.getData()));
					}
				}
				edittext_name.setEnabled(true);
				button_upload.setEnabled(true);
				edittext_name.setAlpha((float)(1));
				button_upload.setAlpha((float)(1));
				textview_path.setText(_filePath.get((int)(0)));
				edittext_name.setText(Uri.parse(_filePath.get((int)(0))).getLastPathSegment().substring((int)(0), (int)(Uri.parse(_filePath.get((int)(0))).getLastPathSegment().lastIndexOf("."))));
				path = _filePath.get((int)(0));
			}
			else {
				
			}
			break;
			default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		if (!isExitDisabled) {
			finish();
		}
	}
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input){
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels(){
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels(){
		return getResources().getDisplayMetrics().heightPixels;
	}
	
}
