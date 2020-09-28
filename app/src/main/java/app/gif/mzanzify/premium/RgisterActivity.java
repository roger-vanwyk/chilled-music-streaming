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
import android.widget.EditText;
import android.widget.Button;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.content.Intent;
import android.net.Uri;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.graphics.Typeface;

public class RgisterActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private HashMap<String, Object> map = new HashMap<>();
	private boolean isLogin = false;
	private String name_var = "";
	private String email_var = "";
	private String reset_mail = "";
	
	private LinearLayout linear1;
	private ImageView imageview1;
	private TextView title_1;
	private TextView title_2;
	private LinearLayout linear2;
	private EditText email;
	private EditText password;
	private EditText password2;
	private TextView text_forgetpass;
	private Button button1;
	private LinearLayout linear3;
	private TextView sign_text;
	private TextView sign_text_act;
	
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
	private Intent i = new Intent();
	private DatabaseReference profile = _firebase.getReference("profile/text");
	private ChildEventListener _profile_child_listener;
	private AlertDialog.Builder dialog;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.rgister);
		com.google.firebase.FirebaseApp.initializeApp(this);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		imageview1 = (ImageView) findViewById(R.id.imageview1);
		title_1 = (TextView) findViewById(R.id.title_1);
		title_2 = (TextView) findViewById(R.id.title_2);
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);
		password2 = (EditText) findViewById(R.id.password2);
		text_forgetpass = (TextView) findViewById(R.id.text_forgetpass);
		button1 = (Button) findViewById(R.id.button1);
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		sign_text = (TextView) findViewById(R.id.sign_text);
		sign_text_act = (TextView) findViewById(R.id.sign_text_act);
		Auth = FirebaseAuth.getInstance();
		dialog = new AlertDialog.Builder(this);
		
		text_forgetpass.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
				LinearLayout mylayout = new LinearLayout(RgisterActivity.this);
				
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				
				mylayout.setLayoutParams(params); mylayout.setOrientation(LinearLayout.VERTICAL);
				
				final EditText myedittext = new EditText(RgisterActivity.this);
				myedittext.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
				 
				mylayout.addView(myedittext);
				dialog.setView(mylayout);
				myedittext.setHint("Enter your Email");
				dialog.setTitle("Reset Password");
				dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						reset_mail = myedittext.getText().toString();
						Auth.sendPasswordResetEmail(reset_mail).addOnCompleteListener(_Auth_reset_password_listener);
						QueryUtil.showMessage(getApplicationContext(), "Check Your Email");
					}
				});
				dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				dialog.create().show();
			}
		});
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (isLogin) {
					if (email.getText().toString().equals("")) {
						QueryUtil.showMessage(getApplicationContext(), "Email Empty!");
					}
					else {
						if (password.getText().toString().equals("")) {
							QueryUtil.showMessage(getApplicationContext(), "Password Empty!");
						}
						else {
							if (password.getText().toString().equals(password2.getText().toString())) {
								email.setEnabled(false);
								password.setEnabled(false);
								password2.setEnabled(false);
								button1.setEnabled(false);
								Auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(RgisterActivity.this, _Auth_sign_in_listener);
							}
							else {
								QueryUtil.showMessage(getApplicationContext(), "Password Doesn't Match!");
							}
						}
					}
				}
				else {
					if (email.getText().toString().equals("")) {
						QueryUtil.showMessage(getApplicationContext(), "Email Empty!");
					}
					else {
						if (password.getText().toString().equals("")) {
							QueryUtil.showMessage(getApplicationContext(), "Password Empty!");
						}
						else {
							if (password.getText().toString().equals(password2.getText().toString())) {
								email.setEnabled(false);
								password.setEnabled(false);
								password2.setEnabled(false);
								button1.setEnabled(false);
								Auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(RgisterActivity.this, _Auth_create_user_listener);
							}
							else {
								QueryUtil.showMessage(getApplicationContext(), "Password Doesn't Match!");
							}
						}
					}
				}
			}
		});
		
		sign_text_act.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (isLogin) {
					isLogin = false;
					button1.setText("Sign Up");
					title_1.setText("Welcome!");
					title_2.setText("Sign Up To Continue");
					text_forgetpass.setVisibility(View.GONE);
					sign_text.setText("Already Sign Up?");
					sign_text_act.setText("Sign In");
				}
				else {
					isLogin = true;
					button1.setText("Sign In");
					title_1.setText("Welcome Back!");
					title_2.setText("Sign In To Continue");
					text_forgetpass.setVisibility(View.VISIBLE);
					sign_text.setText("New User?");
					sign_text_act.setText("Sign Up");
				}
			}
		});
		
		_profile_child_listener = new ChildEventListener() {
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
		profile.addChildEventListener(_profile_child_listener);
		
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
				if (_success) {
					QueryUtil.showMessage(getApplicationContext(), "Sign Up Completed! Now Login");
					FirebaseAuth.getInstance().signOut();
					email.setEnabled(true);
					password.setEnabled(true);
					password2.setEnabled(true);
					button1.setEnabled(true);
				}
				else {
					QueryUtil.showMessage(getApplicationContext(), _errorMessage);
					email.setEnabled(true);
					password.setEnabled(true);
					password2.setEnabled(true);
					button1.setEnabled(true);
				}
			}
		};
		
		_Auth_sign_in_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				if (_success) {
					i.setClass(getApplicationContext(), MainActivity.class);
					startActivity(i);
					finish();
				}
				else {
					QueryUtil.showMessage(getApplicationContext(), _errorMessage);
					email.setEnabled(true);
					password.setEnabled(true);
					password2.setEnabled(true);
					button1.setEnabled(true);
				}
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
		title_1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/googlesans.ttf"), 1);
		title_2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/googlesans.ttf"), 0);
		email.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/googlesans.ttf"), 0);
		password.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/googlesans.ttf"), 0);
		password2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/googlesans.ttf"), 0);
		button1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/googlesans.ttf"), 1);
		sign_text.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/googlesans.ttf"), 0);
		sign_text_act.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/googlesans.ttf"), 0);
		text_forgetpass.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/googlesans.ttf"), 1);
		text_forgetpass.setVisibility(View.GONE);
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			
			default:
			break;
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
