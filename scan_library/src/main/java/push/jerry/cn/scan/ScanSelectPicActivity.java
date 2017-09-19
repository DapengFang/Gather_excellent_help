package push.jerry.cn.scan;

import android.support.v7.app.AppCompatActivity;

public class ScanSelectPicActivity extends AppCompatActivity {
    public static final int REQUEST_CROP = 100;
    public static final int REQUEST_CODE_PHOTO_SELECT_CAMERA = 10;
    public static String selectedImage = "";
//    @BindView(R.id.scan_pic_phone)
//    TextView fromPhone;
//    @BindView(R.id.scan_pic_time)
//    TextView fromTime;
//    @BindView(R.id.scan_viewpager)
//    ViewPager mViewPager;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    File cameraOutPutFile;
//    private List<BasePresenterFragment> fragmentList = new ArrayList<BasePresenterFragment>();
//    private ScanSelectPicActivity.MyFragmentPagerAdapter pagerAdapter;
//    private String printId;
//    private int ratioW;
//    private int ratioH;
//    private TFProgressDialog dialog;
//    private ViewPager.OnPageChangeListener pageListener =
//            new ViewPager.OnPageChangeListener() {
//                @Override
//                public void onPageSelected(int arg0) {
//                    if (arg0 == 0) {
//                        fromPhone.setTextColor(Color.parseColor("#b7b7b7"));
//                        fromTime.setTextColor(Color.parseColor("#ffffff"));
//                    } else if (arg0 == 1) {
//                        fromPhone.setTextColor(Color.parseColor("#ffffff"));
//                        fromTime.setTextColor(Color.parseColor("#b7b7b7"));
//                    }
//                }
//
//                @Override
//                public void onPageScrolled(int arg0, float arg1, int arg2) {
//                }
//
//                @Override
//                public void onPageScrollStateChanged(int arg0) {
//                }
//            };
//
//    public static void open(Context context, String printId, int ratioW, int ratioH) {
//        Intent intent = new Intent(context, ScanSelectPicActivity.class);
//        intent.putExtra("printId", printId);
//        intent.putExtra("ratio_w", ratioW);
//        intent.putExtra("ratio_h", ratioH);
//        context.startActivity(intent);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scan_selectpic);
//        ButterKnife.bind(this);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        printId = getIntent().getStringExtra("printId");
//        ratioW = getIntent().getIntExtra("ratio_w", 1);
//        ratioH = getIntent().getIntExtra("ratio_h", 1);
//        pagerAdapter = new ScanSelectPicActivity.MyFragmentPagerAdapter(getSupportFragmentManager());
//        fragmentList.add(SelectPhoneImageFragment.getInstance());
//        fragmentList.add(SelectTimeImageFragment.getInstance());
//        mViewPager.setAdapter(pagerAdapter);
//        mViewPager.setOnPageChangeListener(pageListener);
//        mViewPager.setCurrentItem(0);
////        dialog = new TFProgressDialog(this, mViewPager);
//        dialog = new TFProgressDialog();
//        dialog.setTvMessage("正在获取图片数据");
//        setClickListener();
//    }
//
//    private void setClickListener() {
//        fromPhone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mViewPager.setCurrentItem(0, true);
//            }
//        });
//        fromTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mViewPager.setCurrentItem(1, true);
//            }
//        });
////        headRight.setOnClickListener(new View.OnClickListener()
////        {
////            @Override
////            public void clickBtn(View v)
////            {
////                Intent camera = new Intent(ScanSelectPicActivity.this,
////                        CameraActivity.class);
////                startActivityForResult(camera, 0x06);
////            }
////        });
//    }
//
//    public void onSelPic(View view) {
//        PhotoModel imageItem = (PhotoModel) view.getTag(R.string.tag_ex);
//        String imgPath = (TextUtils.isEmpty(imageItem.getLocalPath()) && imageItem instanceof ImgObj)
//                ? ((ImgObj) imageItem).getImgPath()
//                : imageItem.getLocalPath();
//        if (imgPath.contains("http")) {
//            dialog.show(getSupportFragmentManager(), "dialog");
//
//            addSubscription(
//                    apiServiceV2.downloadFile(imgPath)
//                            .compose(SchedulersCompat.applyIoSchedulers())
//                            .flatMap(new Func1<ResponseBody, Observable<Boolean>>() {
//                                @Override
//                                public Observable<Boolean> call(ResponseBody responseBody) {
//                                    return Observable.just(writeResponseBodyToDisk(responseBody, imgPath));
//                                }
//                            })
//                            .subscribe(
//                                    aBoolean -> {
//                                        if(aBoolean){
//                                            dialog.dismiss();
//                                            File file = StorageUtil.getTFPhotoPath(imgPath.substring(imgPath.lastIndexOf("/") + 1));
//                                            if (file != null)
//                                                imageItem.setLocalPath(file.getAbsolutePath());
//                                            toCutImage(imageItem);
//                                        } else {
//                                            dialog.dismiss();
//                                            Toast.makeText(this, "获取图片失败，请重新获取！", Toast.LENGTH_SHORT).show();
//                                        }
//                                    },
//                                    throwable -> {
//                                        dialog.dismiss();
//                                        Toast.makeText(this, "获取图片失败，请重新获取！", Toast.LENGTH_SHORT).show();
//                                    }
//                            )
//            );
//        } else {
//            toCutImage(imageItem);
//        }
//    }
//
//    private void toCutImage(PhotoModel item) {
//        //裁剪
////        imageOutFile = BitmapUtil.getPhotoPath();
////        Intent intent = new Intent("com.android.camera.action.CROP");
////        File file = new File(path);
////        BitmapFactory.Options options = new BitmapFactory.Options();
////        options.inJustDecodeBounds = true;
////        BitmapFactory.decodeFile(path, options);
////
////        // aspectX aspectY 是宽高的比例
////        intent.putExtra("aspectX",
////                options.outWidth > options.outHeight ? 1080 : 800);
////        intent.putExtra("aspectY",
////                options.outWidth > options.outHeight ? 800 : 1080);
////
////        intent.setDataAndType(Uri.fromFile(file), "image/*");
////        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
////        intent.putExtra("crop", "true");
////        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageOutFile));
////        //注意  魅族等手机可能存在onResult时不返回data的问题，此时应该直接使用全局变量outfile
////        // outputX outputY 是裁剪图片宽高
////        //intent.putExtra("outputX", 320);
////        //intent.putExtra("outputY", 320);
////        intent.putExtra("return-data", false);
////        act.startActivityForResult(intent, 0x03);
//
//        CropPicActivity.openForResult(this, item, ratioW, ratioH, -1, -1, REQUEST_CROP);
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode,
//                                    Intent data) {
//        if (resultCode != Activity.RESULT_OK) {
//            return;
//        }
//        switch (requestCode) {
//            case REQUEST_CROP:
//                try {
////                //bit = data.getParcelableExtra("data");
////                Uri outFile = data.getData();
////                //String cutPath = BitmapUtil.savePhoto(
////                //        BitmapUtil.bitmap2Bytes(bit, 100));
////                if (outFile == null)
////                {
////                    outFile = Uri.fromFile(imageOutFile);
////                }
////                System.out.println(outFile.getPath() + "******************");
////                System.out.println(printId + "****************");
////                Intent intent = new Intent(ScanSelectPicActivity.this,
////                        SendPrintPicActivity.class);
////                intent.putExtra("print_id", printId);
////                intent.putExtra("print_img_path", outFile.getPath());
////                startActivity(intent);
//
//                    String cropPath = data.getStringExtra("crop_path");
//                    if (cropPath != null)
//                        SendPrintPicActivity.open(this, printId, cropPath);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            case REQUEST_CODE_PHOTO_SELECT_CAMERA:
//
//                PhotoModel photoModel = new ImgObj(cameraOutPutFile.getAbsolutePath(), null);
//                toCutImage(photoModel);
//                break;
//        }
//    }
//
//    private Boolean writeResponseBodyToDisk(ResponseBody body, String imgPath) {
//        try {
//            File futureStudioIconFile = StorageUtil.getTFPhotoPath(imgPath.substring(imgPath.lastIndexOf("/") + 1));
//            InputStream inputStream = null;
//            OutputStream outputStream = null;
//
//            try {
//                byte[] fileReader = new byte[4096];
//
//                long fileSizeDownloaded = 0;
//
//                inputStream = body.byteStream();
//                outputStream = new FileOutputStream(futureStudioIconFile);
//
//                while (true) {
//                    int read = inputStream.read(fileReader);
//                    if (read == -1) {
//                        break;
//                    }
//                    outputStream.write(fileReader, 0, read);
//                    fileSizeDownloaded += read;
//                }
//
//                outputStream.flush();
//
//                return true;
//            } catch (IOException e) {
//                return false;
//            } finally {
//                if (inputStream != null) {
//                    inputStream.close();
//                }
//                if (outputStream != null) {
//                    outputStream.close();
//                }
//            }
//        } catch (IOException e) {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_activity_scan_sel_pic_take_photo, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.take_photo:
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                cameraOutPutFile = StorageUtil.genSystemPhotoFile();
//                intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(cameraOutPutFile));
//                startActivityForResult(intent,
//                        REQUEST_CODE_PHOTO_SELECT_CAMERA);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        UmsAgent.onResume(this, UmsConstants.MODULE_PERSONAL_CENTER + this.getClass().getSimpleName());
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        UmsAgent.onPause(this);
//    }
//
//    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
//
//        public MyFragmentPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int arg0) {
//            return fragmentList.get(arg0);
//        }
//
//        @Override
//        public int getCount() {
//            return fragmentList.size();
//        }
//    }
}

