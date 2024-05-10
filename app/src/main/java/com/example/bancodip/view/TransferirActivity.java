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
        Double chequeUser = controllerBancoDados.getChequeByTitular(emailUser);

        binding.btnTransferirUser.setOnClickListener(v -> {
            String destinatarioEmail = binding.transUserEmail.getText().toString().toUpperCase();
            Double destinatarioSaldo = controllerBancoDados.getSaldoByTitular(destinatarioEmail);
            String valorUser = binding.transUserValor.getText().toString();

            if (controllerBancoDados.isEmailInDatabase(destinatarioEmail)) {
                try {
                    Double valorTransferencia = Double.parseDouble(valorUser);

                    if (saldoUser >= valorTransferencia) {
                        // Saldo é suficiente para a transferência
                        Double saldoUserNew = saldoUser - valorTransferencia;
                        Double saldoDestinatarioNew = destinatarioSaldo + valorTransferencia;

                        controllerBancoDados.updateSaldo(destinatarioEmail, saldoDestinatarioNew);
                        controllerBancoDados.updateSaldo(emailUser, saldoUserNew);
                    } else if (chequeUser >= valorTransferencia) {
                        // Usa o cheque especial para cobrir a diferença
                        Double saldoUserNew = 0.0;
                        Double chequeUserNew = chequeUser - valorTransferencia + saldoUser;
                        Double saldoDestinatarioNew = destinatarioSaldo + valorTransferencia;

                        controllerBancoDados.updateSaldo(destinatarioEmail, saldoDestinatarioNew);
                        controllerBancoDados.updateSaldo(emailUser, saldoUserNew);
                        controllerBancoDados.updateCheque(emailUser, chequeUserNew);
                    } else {
                        // Saldo e cheque especial insuficientes
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("SALDO E CHEQUE ESPECIAL INSUFICIENTES PARA REALIZAR A TRANSFERÊNCIA");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Nada aqui
                            }
                        });
                        AlertDialog alerta = builder.create();
                        alerta.show();
                        return;
                    }

                    // Transferência efetuada com sucesso
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("TRANSFERÊNCIA EFETUADA COM SUCESSO");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Nada aqui
                        }
                    });
                    AlertDialog alerta = builder.create();
                    alerta.show();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    controllerBancoDados.close();
                    binding.transUserValor.setText("");
                    binding.transUserEmail.setText("");
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("EMAIL DO DESTINATÁRIO NÃO CADASTRADO");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Nada aqui
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