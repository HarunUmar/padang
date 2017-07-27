package com.hitesh_sahu.retailapp.util;

/**
 * Created by root on 5/30/2017.
 */

public interface Template {



    interface Query{

        //Penggunaan Key dan Value untuk Map<,> dan keperluan difile php

        String tag_json_obj = "json_obj_req";

        //tag pesanan

        public static final String TAG_NO           = "no";
        public static final String TAG_ID           = "id";
        public static final String TAG_ID_USERS     = "id_users";
        public static final String TAG_ID_RM        = "id_rm";
        public static final String TAG_MENU         = "id_menu";
        public static final String TAG_EMAIL         = "email";


        public static final String TAG_NAMA         = "nama";
        public static final String TAG_JUDUL        = "judul";
        public static final String TAG_TGL          = "tgl";
        public static final String TAG_JAM          = "jam";
        public static final String TAG_PESAN        = "pesan";
        public static final String TAG_HARGA        = "harga";
        public static final String TAG_IMAGE        = "image";
        public static final String TAG_ID_KATAGORI  = "id_katagori";
        public static final String TAG_ID_MENU  = "id_menu";
        public static final String TAG_JUMLAH  = "jumlah";
        public static final String TAG_SALDO  = "saldo";
        public static final String TAG_STATUS  = "status";
        public static final String TAG_BAYAR  = "bayar";
        public static final String TAG_KET  = "ket";
        public static final String TAG_LATITUDE = "lat";
        public static final String TAG_LANGTITUDE = "lang";









        public static final String TAG_SUCCESS      = "success";
        public static final String TAG_MESSAGE      = "message";


    }




}