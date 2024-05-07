package com.example.bancodip.controller;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.bancodip.model.ModelBancoDados;

public class ControllerBancoDados {

    private final Context context;
    private SQLiteDatabase database;
    private final ModelBancoDados dbHelper;

    public ControllerBancoDados(Context context) {
        this.context = context;
        dbHelper = new ModelBancoDados(context);
    }

    public ControllerBancoDados open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insertData(String name, String email, double saldo, double chequeEspecial, double chequeEspecialDefi) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ModelBancoDados.COLUNA_TITULAR, name);
        contentValues.put(ModelBancoDados.COLUNA_EMAIL, email);
        contentValues.put(ModelBancoDados.COLUNA_SALDO, saldo);
        contentValues.put(ModelBancoDados.COLUNA_CHEQUE_ESPECIAL_DEFI, chequeEspecialDefi);
        contentValues.put(ModelBancoDados.COLUNA_CHEQUE_ESPECIAL, chequeEspecial);

        long result = -1;

        try {
            result = database.insertOrThrow(ModelBancoDados.NOME_TABELA, null, contentValues);
            Log.d("INSERT_DATA", "Inserção bem-sucedida. ID do novo registro: " + result);
        } catch (SQLException e) {
            Log.e("INSERT_DATA", "Erro ao inserir dados: " + e.getMessage());
        }

        return result;
    }

    public void updateSaldo(String email, double newSaldo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ModelBancoDados.COLUNA_SALDO, newSaldo);
        String whereClause = ModelBancoDados.COLUNA_EMAIL + " = ?";
        String[] whereArgs = {email};
        database.update(ModelBancoDados.NOME_TABELA, contentValues, whereClause, whereArgs);
    }

    public void updateCheque(String email, double newCheque) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ModelBancoDados.COLUNA_CHEQUE_ESPECIAL, newCheque);
        String whereClause = ModelBancoDados.COLUNA_EMAIL + " = ?";
        String[] whereArgs = {email};
        database.update(ModelBancoDados.NOME_TABELA, contentValues, whereClause, whereArgs);
    }

    public Cursor getAllData() {
        return database.query(ModelBancoDados.NOME_TABELA,
                new String[]{ModelBancoDados.COLUNA_ID, ModelBancoDados.COLUNA_TITULAR, ModelBancoDados.COLUNA_SALDO, ModelBancoDados.COLUNA_EMAIL},
                null, null, null, null, null);
    }

    public Double getSaldoByTitular(String email) {
        Double saldo = 0.0;
        try (Cursor cursor = database.query(ModelBancoDados.NOME_TABELA,
                new String[]{ModelBancoDados.COLUNA_SALDO},
                ModelBancoDados.COLUNA_EMAIL + " = ?",
                new String[]{email},
                null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                int saldoIndex = cursor.getColumnIndex(ModelBancoDados.COLUNA_SALDO);
                saldo = cursor.getDouble(saldoIndex);
            }
        } catch (Exception e) {
            Log.e("GET_SALDO", "Erro ao obter saldo: " + e.getMessage());
        }
        return saldo;
    }

    public Double getChequeByTitular(String email) {
        Double cheque = 0.0;
        try (Cursor cursor = database.query(ModelBancoDados.NOME_TABELA,
                new String[]{ModelBancoDados.COLUNA_CHEQUE_ESPECIAL},
                ModelBancoDados.COLUNA_EMAIL + " = ?",
                new String[]{email},
                null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                int chequeIndex = cursor.getColumnIndex(ModelBancoDados.COLUNA_CHEQUE_ESPECIAL);
                if (!cursor.isNull(chequeIndex)) {
                    cheque = cursor.getDouble(chequeIndex);
                }
            }
        } catch (Exception e) {
            Log.e("GET_CHEQUE", "Erro ao obter cheque especial: " + e.getMessage());
        }
        return cheque;
    }

    public Double getChequeDEFIByTitular(String email) {
        Double cheque = 0.0;
        try (Cursor cursor = database.query(ModelBancoDados.NOME_TABELA,
                new String[]{ModelBancoDados.COLUNA_CHEQUE_ESPECIAL_DEFI},
                ModelBancoDados.COLUNA_EMAIL + " = ?",
                new String[]{email},
                null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                int chequeIndex = cursor.getColumnIndex(ModelBancoDados.COLUNA_CHEQUE_ESPECIAL_DEFI);
                if (!cursor.isNull(chequeIndex)) {
                    cheque = cursor.getDouble(chequeIndex);
                }
            }
        } catch (Exception e) {
            Log.e("GET_CHEQUE", "Erro ao obter cheque especial: " + e.getMessage());
        }
        return cheque;
    }

    public boolean isEmailInDatabase(String emailToCheck) {
        try (Cursor cursor = database.query(
                ModelBancoDados.NOME_TABELA,
                new String[]{ModelBancoDados.COLUNA_EMAIL},
                null, null, null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(ModelBancoDados.COLUNA_EMAIL));
                    if (emailToCheck.equals(email)) {
                        return true;
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("EMAIL_IN_DB", "Erro ao verificar email na base de dados: " + e.getMessage());
        }
        return false;
    }

    public boolean isNomeInDatabase(String nameToCheck) {
        try (Cursor cursor = database.query(
                ModelBancoDados.NOME_TABELA,
                new String[]{ModelBancoDados.COLUNA_TITULAR},
                null, null, null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String nome = cursor.getString(cursor.getColumnIndex(ModelBancoDados.COLUNA_TITULAR));
                    if (nameToCheck.equals(nome)) {
                        return true;
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("NOME_IN_DB", "Erro ao verificar nome na base de dados: " + e.getMessage());
        }
        return false;
    }

}
