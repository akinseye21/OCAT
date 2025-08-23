package com.example.ocat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GraphPage extends AppCompatActivity {

    LinearLayout mainLayout;
    TextView moduleCategory;
    TextView score, range, interpretation;
    RelativeLayout btn_download_report, btn_share_report;
    String category;
    PieChart pieChart;
    int[] colorClassArray = new int[]{Color.GREEN, Color.LTGRAY};
    ArrayList<String> values;
    int max_point, current_point = 0;
    float percentagePass;
    float percentageFail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_page);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        values = i.getStringArrayListExtra("values");
        category = i.getStringExtra("category");
        System.out.println("The values = "+values);

        moduleCategory = findViewById(R.id.moduleCategory);
        moduleCategory.setText("MODULE : "+category+" - Report and Analysis");

        mainLayout = findViewById(R.id.main);
        score = findViewById(R.id.score);
        range = findViewById(R.id.range);
        interpretation = findViewById(R.id.interpretation);
        btn_download_report = findViewById(R.id.downloadReport);
        btn_share_report = findViewById(R.id.shareReport);

        for (int j=0; j<values.size(); j++){
            current_point = current_point + Integer.parseInt(values.get(j));
        }

        int arraysize = values.size();
        max_point = arraysize*5;

        percentagePass = ((float) current_point / max_point) * 100;
        percentageFail = 100 - percentagePass;

        System.out.println("Max point = "+max_point+"\nCurrent point = "+current_point);
        System.out.println("Percentage pass = "+percentagePass+"\nPercentage fail = "+percentageFail);

        score.setText(percentagePass+"%");
        if (percentagePass<=25){
            score.setTextColor(getResources().getColor(R.color.red));
            range.setText("Critical");
            range.setTextColor(getResources().getColor(R.color.red));
            interpretation.setText("Prone to adverse risks and shocks and unable to sustain operations due to the absence of strong systems and structures");
            interpretation.setTextColor(getResources().getColor(R.color.red));
        } else if (percentagePass > 25 && percentagePass <= 50) {
            score.setTextColor(getResources().getColor(R.color.blue));
            range.setText("Striving");
            range.setTextColor(getResources().getColor(R.color.blue));
            interpretation.setText("Presence of basic operational structures to sustain community engagement without being able to ensure long-term sustainability of operations and interventions");
            interpretation.setTextColor(getResources().getColor(R.color.blue));
        } else if (percentagePass > 50 && percentagePass <= 75) {
            score.setTextColor(getResources().getColor(R.color.green));
            range.setText("Thriving");
            range.setTextColor(getResources().getColor(R.color.green));
            interpretation.setText("Evidence of organising systems and structures, able to deliver services and satisfactorily engage with members and communities");
            interpretation.setTextColor(getResources().getColor(R.color.green));
        }else{
            score.setTextColor(getResources().getColor(R.color.yellow));
            range.setText("Viable");
            range.setTextColor(getResources().getColor(R.color.yellow));
            interpretation.setText("Capacity to withstand shocks and risk, capacity to innovate, nurture community-led initiatives, scale up and effectivley engage with members and communities");
            interpretation.setTextColor(getResources().getColor(R.color.yellow));
        }


        pieChart = findViewById(R.id.pieChart);
        PieDataSet pieDataSet = new PieDataSet(dataValues1(), "");
        pieDataSet.setColors(colorClassArray);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setUsePercentValues(false);
        pieChart.setDrawEntryLabels(true);
        pieChart.setCenterText("% diff.");
        pieChart.setCenterTextSize(15);
        pieChart.setCenterTextRadiusPercent(50);
        pieChart.setHoleRadius(40);
        pieChart.setTransparentCircleRadius(50);
        pieChart.setTransparentCircleColor(Color.YELLOW);
        pieChart.setTransparentCircleAlpha(50);
        pieChart.setMaxAngle(360);
        pieChart.setEntryLabelTextSize(10);
        pieChart.setData(pieData);
        pieChart.invalidate();


        btn_download_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPdfFromLayout(mainLayout);
            }
        });
        btn_share_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePdfFile();
            }
        });
    }

    private void createPdfFromLayout(View view) {
        // Create PDF document
        PdfDocument document = new PdfDocument();
        // Page info (size same as view)
        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(view.getWidth(), view.getHeight(), 1).create();
        // Start page
        PdfDocument.Page page = document.startPage(pageInfo);
        // Draw the view onto the page
        view.draw(page.getCanvas());
        document.finishPage(page);

        // Save PDF in device storage
        String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(directoryPath, "OCAT_Report_"+category+".pdf");
        try {
            document.writeTo(new FileOutputStream(file));
            System.out.println("PDF saved at: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        document.close();
    }

    private void sharePdfFile(){
        String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(directoryPath, "OCAT_Report_"+category+".pdf");

        if (!file.exists()) {
            // Handle case where PDF doesn’t exist yet
            Dialog myDialog = new Dialog(GraphPage.this);
            myDialog.setContentView(R.layout.custom_popup_unabletoshare);
            ImageView close = myDialog.findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.setCanceledOnTouchOutside(true);
            myDialog.show();
            return;
        }

        // Use FileProvider for secure sharing
        Uri pdfUri = FileProvider.getUriForFile(
                this,
                getPackageName() + ".fileprovider", // must match Manifest authority
                file
        );

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/pdf");
        shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(shareIntent, "Share PDF using"));
    }

    private ArrayList<PieEntry> dataValues1(){
        ArrayList<PieEntry> dataVals = new ArrayList<PieEntry>();
        dataVals.add(new PieEntry(percentagePass, "% Pass"));
        dataVals.add(new PieEntry(percentageFail, "% Fail"));

        return dataVals;
    }
}