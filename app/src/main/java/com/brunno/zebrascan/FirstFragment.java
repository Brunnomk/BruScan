package com.brunno.zebrascan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.brunno.zebrascan.databinding.FragmentFirstBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private final ArrayList<Leitura> historico = new ArrayList<>();
    private File ultimoArquivoCsv = null;

    private final Handler autoScanHandler = new Handler(Looper.getMainLooper());
    private Runnable autoScanRunnable;
    private boolean limpandoCampoCodigo = false;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.edittextCodigo.requestFocus();

        binding.edittextCodigo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (limpandoCampoCodigo) {
                    return;
                }

                String codigoDigitado = editable.toString().trim();

                if (codigoDigitado.isEmpty()) {
                    return;
                }

                if (autoScanRunnable != null) {
                    autoScanHandler.removeCallbacks(autoScanRunnable);
                }

                autoScanRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (binding != null) {
                            String codigoAtual = binding.edittextCodigo.getText().toString().trim();

                            if (!codigoAtual.isEmpty()) {
                                confirmarLeituraAutomatica();
                            }
                        }
                    }
                };

                autoScanHandler.postDelayed(autoScanRunnable, 450);
            }
        });

        binding.edittextCodigo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean apertouEnter = keyEvent != null
                        && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER
                        && keyEvent.getAction() == KeyEvent.ACTION_DOWN;

                boolean apertouDone = actionId == EditorInfo.IME_ACTION_DONE;

                if (apertouEnter || apertouDone) {
                    confirmarLeituraAutomatica();
                    return true;
                }

                return false;
            }
        });

        binding.buttonDesfazerUltima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desfazerUltimaLeitura();
            }
        });

        binding.buttonExportarCsv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File arquivo = exportarCsvParaArquivo();

                if (arquivo != null) {
                    ultimoArquivoCsv = arquivo;
                    binding.textviewStatusExportacao.setText(
                            "CSV exportado com sucesso:\n" + arquivo.getAbsolutePath()
                    );
                }
            }
        });

        binding.buttonCompartilharCsv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compartilharCsv();
            }
        });

        binding.buttonLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                historico.clear();
                ultimoArquivoCsv = null;

                binding.textviewHistorico.setText("Nenhuma leitura confirmada ainda.");
                binding.textviewResumoProdutos.setText("Nenhum produto nomeado ainda.");
                binding.textviewCodigo.setText("Último código lido: ---");
                binding.textviewStatusExportacao.setText("Status: histórico limpo.");

                limparCampoCodigo();
                binding.edittextProduto.setText("");

                binding.edittextCodigo.requestFocus();
            }
        });
    }

    private void confirmarLeituraAutomatica() {
        if (autoScanRunnable != null) {
            autoScanHandler.removeCallbacks(autoScanRunnable);
        }

        String produto = binding.edittextProduto.getText().toString().trim();
        String codigo = binding.edittextCodigo.getText().toString().trim();

        if (codigo.isEmpty()) {
            binding.edittextCodigo.requestFocus();
            return;
        }

        String data = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String hora = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        Leitura leitura = new Leitura(data, hora, produto, codigo);

        historico.add(0, leitura);
        ultimoArquivoCsv = null;

        if (produto.isEmpty()) {
            binding.textviewCodigo.setText(
                    "Último código lido:\n" + codigo
            );
        } else {
            binding.textviewCodigo.setText(
                    "Produto: " + produto +
                            "\nÚltimo código lido:\n" + codigo
            );
        }

        binding.textviewStatusExportacao.setText("Status: leitura adicionada automaticamente.");

        atualizarHistorico();
        atualizarResumoProdutos();

        limparCampoCodigo();
        binding.edittextCodigo.requestFocus();
    }

    private void desfazerUltimaLeitura() {
        if (historico.isEmpty()) {
            binding.textviewStatusExportacao.setText("Status: não existe leitura para desfazer.");
            return;
        }

        Leitura leituraRemovida = historico.remove(0);
        ultimoArquivoCsv = null;

        binding.textviewStatusExportacao.setText(
                "Status: última leitura removida:\n" + leituraRemovida.codigo
        );

        if (historico.isEmpty()) {
            binding.textviewHistorico.setText("Nenhuma leitura confirmada ainda.");
            binding.textviewResumoProdutos.setText("Nenhum produto nomeado ainda.");
            binding.textviewCodigo.setText("Último código lido: ---");
        } else {
            Leitura ultimaLeitura = historico.get(0);

            if (ultimaLeitura.produto.isEmpty()) {
                binding.textviewCodigo.setText(
                        "Último código lido:\n" + ultimaLeitura.codigo
                );
            } else {
                binding.textviewCodigo.setText(
                        "Produto: " + ultimaLeitura.produto +
                                "\nÚltimo código lido:\n" + ultimaLeitura.codigo
                );
            }

            atualizarHistorico();
            atualizarResumoProdutos();
        }

        binding.edittextCodigo.requestFocus();
    }

    private void limparCampoCodigo() {
        limpandoCampoCodigo = true;
        binding.edittextCodigo.setText("");
        limpandoCampoCodigo = false;
    }

    private void atualizarHistorico() {
        if (historico.isEmpty()) {
            binding.textviewHistorico.setText("Nenhuma leitura confirmada ainda.");
            return;
        }

        StringBuilder textoHistorico = new StringBuilder();

        for (int i = 0; i < historico.size(); i++) {
            Leitura leitura = historico.get(i);

            textoHistorico.append(i + 1)
                    .append(". ")
                    .append(leitura.hora)
                    .append(" | ");

            if (!leitura.produto.isEmpty()) {
                textoHistorico.append("Produto: ")
                        .append(leitura.produto)
                        .append(" | ");
            }

            textoHistorico.append("Código: ")
                    .append(leitura.codigo)
                    .append("\n");
        }

        binding.textviewHistorico.setText(textoHistorico.toString());
    }

    private void atualizarResumoProdutos() {
        Map<String, Integer> resumo = new LinkedHashMap<>();

        for (Leitura leitura : historico) {
            if (!leitura.produto.isEmpty()) {
                int quantidadeAtual = 0;

                if (resumo.containsKey(leitura.produto)) {
                    quantidadeAtual = resumo.get(leitura.produto);
                }

                resumo.put(leitura.produto, quantidadeAtual + 1);
            }
        }

        if (resumo.isEmpty()) {
            binding.textviewResumoProdutos.setText("Nenhum produto nomeado ainda.");
            return;
        }

        StringBuilder textoResumo = new StringBuilder();

        for (Map.Entry<String, Integer> item : resumo.entrySet()) {
            textoResumo.append(item.getKey())
                    .append(": ")
                    .append(item.getValue())
                    .append(" códigos lidos")
                    .append("\n");
        }

        binding.textviewResumoProdutos.setText(textoResumo.toString());
    }

    private File exportarCsvParaArquivo() {
        if (historico.isEmpty()) {
            binding.textviewStatusExportacao.setText("Status: nenhuma leitura para exportar.");
            return null;
        }

        String nomeArquivo = "bruscan_historico_" +
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) +
                ".csv";

        File pasta = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        if (pasta == null) {
            binding.textviewStatusExportacao.setText("Status: erro ao acessar a pasta de documentos.");
            return null;
        }

        File arquivoCsv = new File(pasta, nomeArquivo);

        try {
            OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(arquivoCsv),
                    StandardCharsets.UTF_8
            );

            writer.write('\uFEFF');

            writer.append("data;hora;produto;codigo\n");

            for (int i = historico.size() - 1; i >= 0; i--) {
                Leitura leitura = historico.get(i);

                writer.append(escaparCsv(leitura.data))
                        .append(";")
                        .append(escaparCsv(leitura.hora))
                        .append(";")
                        .append(escaparCsv(leitura.produto))
                        .append(";")
                        .append(escaparCsv(leitura.codigo))
                        .append("\n");
            }

            writer.flush();
            writer.close();

            return arquivoCsv;

        } catch (IOException e) {
            binding.textviewStatusExportacao.setText(
                    "Status: erro ao exportar CSV:\n" + e.getMessage()
            );
            return null;
        }
    }

    private void compartilharCsv() {
        if (historico.isEmpty()) {
            binding.textviewStatusExportacao.setText("Status: nenhuma leitura para compartilhar.");
            return;
        }

        if (ultimoArquivoCsv == null || !ultimoArquivoCsv.exists()) {
            ultimoArquivoCsv = exportarCsvParaArquivo();
        }

        if (ultimoArquivoCsv == null || !ultimoArquivoCsv.exists()) {
            binding.textviewStatusExportacao.setText("Status: não foi possível gerar o CSV.");
            return;
        }

        Uri arquivoUri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().getPackageName() + ".fileprovider",
                ultimoArquivoCsv
        );

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Histórico BruScan");
        intent.putExtra(Intent.EXTRA_TEXT, "Segue em anexo o histórico de leituras do BruScan.");
        intent.putExtra(Intent.EXTRA_STREAM, arquivoUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(intent, "Compartilhar CSV"));

        binding.textviewStatusExportacao.setText("Status: CSV pronto para compartilhar.");
    }

    private String escaparCsv(String valor) {
        if (valor == null) {
            return "";
        }

        String valorCorrigido = valor.replace("\"", "\"\"");

        if (valorCorrigido.contains(";") || valorCorrigido.contains("\"") || valorCorrigido.contains("\n")) {
            return "\"" + valorCorrigido + "\"";
        }

        return valorCorrigido;
    }

    private static class Leitura {
        String data;
        String hora;
        String produto;
        String codigo;

        Leitura(String data, String hora, String produto, String codigo) {
            this.data = data;
            this.hora = hora;
            this.produto = produto;
            this.codigo = codigo;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (autoScanRunnable != null) {
            autoScanHandler.removeCallbacks(autoScanRunnable);
        }

        binding = null;
    }
}