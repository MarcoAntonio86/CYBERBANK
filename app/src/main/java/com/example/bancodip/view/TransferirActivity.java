package com.example.bancodip.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.bancodip.R;
import com.example.bancodip.controller.ControllerBancoDados;
import com.example.bancodip.databinding.ActivityTransferirBinding;

public class TransferirActivity extends AppCompatActivity {

    private ActivityTransferirBinding binding;
    private ControllerBancoDados controllerBancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransferirBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        controllerBancoDados = new ControllerBancoDados(this);

        controllerBancoDados.open();

        Intent intent = getIntent();
        String emailUser = intent.getStringExtra("email_trans");
        Double saldoUser = controllerBancoDados.getSaldoByTitular(emailUser);

        binding.btnTransferirUser.setOnClickListener(v -> {

            String destinatarioEmail = binding.transUserEmail.getText().toString().toUpperCase();
            Double destinatarioSaldo = controllerBancoDados.getSaldoByTitular(destinatarioEmail);
            String valorUser = binding.transUserValor.getText().toString();

            if(controllerBancoDados.isEmailInDatabase(destinatarioEmail) && saldoUser > 0){
                try {

                    Double saldoUserNew = saldoUser - Double.parseDouble(valorUser);
                    Double saldoDestinatarioNew = destinatarioSaldo + Double.parseDouble(valorUser);

                    controllerBancoDados.updateSaldo(destinatarioEmail, saldoDestinatarioNew);
                    controllerBancoDados.updateSaldo(emailUser, saldoUserNew);

                } catch (Exception e){
                    e.printStackTrace();
                } finally {
                    controllerBancoDados.close();
                    binding.transUserValor.setText("");
                    binding.transUserEmail.setText("");

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("CYBER BANK");
                    builder.setMessage("O CYBER BANK Agradece sua preferência");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Nada aqui
                        }
                    });

                    AlertDialog alerta = builder.create();
                    alerta.show();

                }
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("CYBER BANK");
                builder.setMessage("Saldo insuficiente ou email invalido");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // nada aqui
                    }
                });

                AlertDialog alerta = builder.create();
                alerta.show();
            }


        });

        binding.btnVoltar.setOnClickListener(v -> {
            finish();

        });

    }



}