package com.example.ocat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.ocat.AssessmentPage2;
import com.example.ocat.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class FAQAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> question;
    private ArrayList<String> answer;

    boolean isExpanded = false;

    public FAQAdapter(Context context, ArrayList<String> question, ArrayList<String> answer) {
        this.context = context;
        this.question = question;
        this.answer = answer;
    }


    @Override
    public int getCount() {
        return question.size();
    }

    @Override
    public Object getItem(int position) {
        return question.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_faq, parent, false);
        }

        TextView txtQuestion = convertView.findViewById(R.id.txt_question);
        TextView txtAnswer = convertView.findViewById(R.id.txt_answer);
        ImageView imgToggle = convertView.findViewById(R.id.img_toggle);
        MaterialCardView faqCard = convertView.findViewById(R.id.faq_card);

        txtQuestion.setText(question.get(position));
        txtAnswer.setText(answer.get(position));

        // Define your colors
        int[] cardColors = {
                ContextCompat.getColor(context, R.color.light_green),
                ContextCompat.getColor(context, R.color.blue)
        };

        // Pick random color
        Random random = new Random();
        int randomIndex = random.nextInt(cardColors.length);
        faqCard.setCardBackgroundColor(cardColors[randomIndex]);

        faqCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    // collapse
                    txtAnswer.setVisibility(View.GONE);
                    imgToggle.setImageResource(R.drawable.ic_add); // show '+'
                    isExpanded = false;
                } else {
                    // expand
                    txtAnswer.setVisibility(View.VISIBLE);
                    imgToggle.setImageResource(R.drawable.ic_remove); // show '-'
                    isExpanded = true;
                }
            }
        });


        return convertView;
    }
}
