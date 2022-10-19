package com.landon.nfcapplication;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.landon.nfcapplication.adapter.SnAdapter;
import com.landon.nfcapplication.bean.CardSnInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private List<CardSnInfo> mSnList = new ArrayList<>();
    private SnAdapter mSnAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取NfcAdapter实例
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //获取通知
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        //如果获取不到则不支持NFC
        if (nfcAdapter == null) {
            Toast.makeText(MainActivity.this,"设备不支持NFC", Toast.LENGTH_LONG).show();
            return;
        }
        //如果获取到的为不可用状态则未启用NFC
        if (nfcAdapter!=null&&!nfcAdapter.isEnabled()) {
            Toast.makeText(MainActivity.this,"请在系统设置中先启用NFC功能",Toast.LENGTH_LONG).show();
            return;
        }
        //因为启动模式是singleTop，于是会调用onNewIntent方法
        onNewIntent(getIntent());
        CardSnInfo cardSnInfo = new CardSnInfo(0, "Sn序列号", "读取时间");
        mSnList.add(cardSnInfo);
        mSnAdapter = new SnAdapter(MainActivity.this, R.layout.sn_item, mSnList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(mSnAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                Fruit fruit = fruitList.get(position);
//                Toast.makeText(MainActivity.this, fruit.getName(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //获取、传递、解析intent对象，intent中携带卡对象
        readNFCTag(intent);
    }

    private void readNFCTag(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msgs[] = null;
        if (rawMsgs != null) {
            msgs = new NdefMessage[rawMsgs.length];
            //标签可能存储了多个NdefMessage对象，普通情况下仅仅有一个NdefMessage对象
            for (int i = 0; i < rawMsgs.length; i++) {
                //转换成NdefMessage对象
                msgs[i] = (NdefMessage) rawMsgs[i];
            }
        }
        try {
            if (msgs != null) {
                //程序中仅仅考虑了1个NdefRecord对象。若是通用软件应该考虑全部的NdefRecord对象
                NdefRecord record = msgs[0].getRecords()[0];
                //分析第1个NdefRecorder，并创建TextRecord对象
                TextRecord textRecord = TextRecord.parse(msgs[0].getRecords()[0]);
                if(mSnList.size() < 10) {
                    for (int i = 0; i < mSnList.size(); i++) {
                        if (mSnList.get(i).getSn().equals(textRecord.getText())) {
                            Toast.makeText(MainActivity.this, "此sn已经被录入过了", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    int id = mSnList.size();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                    Calendar calendar = Calendar.getInstance();
                    String curTime = formatter.format(calendar.getTime());
                    CardSnInfo cardSnInfo = new CardSnInfo(id, textRecord.getText(), curTime);
                    mSnList.add(cardSnInfo);
                    mSnAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null)
            //设置程序不优先处理
            nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null)
            //设置程序优先处理
            nfcAdapter.enableForegroundDispatch(this, pendingIntent,
                    null, null);
    }

    public void exportSn(View v){
        Log.i("Landon","exportSn");
    }
}