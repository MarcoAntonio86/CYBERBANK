package com.example.bancodip.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ModelBancoDados extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BancoDILP.db";
    private static final int DATABASE_VERSION = 1;

    public static final String NOME_TABELA = "BancoDIP";
    public static final String COLUNA_ID = "id";
    public static final String COLUNA_TITULAR = "titular";
    public static final String COLUNA_EMAIL = "email";
    public static final String COLUNA_SALDO= "saldo";
    public static final String COLUNA_CHEQUE_ESPECIAL = "cheque_especial";
    public static final String COLUNA_CHEQUE_ESPECIAL_DEFI = "cheque_especial_defi";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + NOME_TABELA + " (" +
                    COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUNA_TITULAR + " VARCHAR(80), " +
                    COLUNA_EMAIL + " VARCHAR(80), " +
                    COLUNA_CHEQUE_ESPECIAL + " DECIMAL(10,2), " +
                    COLUNA_CHEQUE_ESPECIAL_DEFI + " DECIMAL(10,2), " +
                    COLUNA_SALDO + " DECIMAL(10,2)" +
                    ");";



    public ModelBancoDados(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NOME_TABELA);
        onCreate(sqLiteDatabase);
    }
}
