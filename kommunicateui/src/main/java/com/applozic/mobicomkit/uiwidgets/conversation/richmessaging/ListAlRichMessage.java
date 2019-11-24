package com.applozic.mobicomkit.uiwidgets.conversation.richmessaging;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applozic.mobicomkit.api.conversation.Message;
import com.applozic.mobicomkit.uiwidgets.AlCustomizationSettings;
import com.applozic.mobicomkit.uiwidgets.R;
import com.applozic.mobicomkit.uiwidgets.conversation.richmessaging.adapters.AlListRMAdapter;
import com.applozic.mobicomkit.uiwidgets.conversation.richmessaging.adapters.AlRichMessageAdapterFactory;
import com.applozic.mobicomkit.uiwidgets.conversation.richmessaging.callbacks.ALRichMessageListener;
import com.applozic.mobicomkit.uiwidgets.conversation.richmessaging.models.ALRichMessageModel;
import com.applozic.mobicommons.json.GsonUtils;
import com.bumptech.glide.Glide;

import java.util.List;

public class ListAlRichMessage extends AlRichMessage {

    public ListAlRichMessage(Context context, LinearLayout containerView, Message message, ALRichMessageListener listener, AlCustomizationSettings alCustomizationSettings) {
        super(context, containerView, message, listener, alCustomizationSettings);
    }

    @Override
    public void createRichMessage() {
        super.createRichMessage();
        LinearLayout listItemLayout = containerView.findViewById(R.id.alListMessageLayout);
        listItemLayout.setVisibility(View.VISIBLE);
        containerView.findViewById(R.id.alRichMessageContainer).setVisibility(View.GONE);
        containerView.findViewById(R.id.alFaqLayout).setVisibility(View.GONE);
        containerView.findViewById(R.id.alFaqReplyLayout).setVisibility(View.GONE);
        containerView.findViewById(R.id.alQuickReplyRecycler).setVisibility(View.GONE);
        setupAlRichMessage(listItemLayout, model);
    }

    @Override
    protected void setupAlRichMessage(ViewGroup listItemLayout, ALRichMessageModel model) {
        super.setupAlRichMessage(listItemLayout, model);
        if (model != null) {
            if (model.getPayload() != null) {
                TextView headerText = listItemLayout.findViewById(R.id.headerText);
                ImageView headerImage = listItemLayout.findViewById(R.id.headerImage);
                ALRichMessageModel.ALPayloadModel payload = (ALRichMessageModel.ALPayloadModel) GsonUtils.getObjectFromJson(model.getPayload(), ALRichMessageModel.ALPayloadModel.class);
                if (payload != null) {
                    RecyclerView listRecycler = listItemLayout.findViewById(R.id.alListItemRecycler);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    listRecycler.setLayoutManager(layoutManager);
                    AlListRMAdapter adapter = (AlListRMAdapter) AlRichMessageAdapterFactory.getInstance().getListRMAdapter(context, message, payload.getElements(), payload.getReplyMetadata(), listener);
                    listRecycler.setAdapter(adapter);

                    if (!TextUtils.isEmpty(payload.getHeaderText())) {
                        headerText.setVisibility(View.VISIBLE);
                        headerText.setText(getHtmlText(payload.getHeaderText()));
                    } else {
                        headerText.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(payload.getHeaderImgSrc())) {
                        headerImage.setVisibility(View.VISIBLE);
                        Glide.with(context).load(payload.getHeaderImgSrc()).into(headerImage);
                    } else {
                        headerImage.setVisibility(View.GONE);
                    }

                    if (payload.getButtons() != null) {
                        final List<ALRichMessageModel.AlButtonModel> action = payload.getButtons();

                        if (action.get(0) != null) {
                            final TextView actionText1 = listItemLayout.findViewById(R.id.actionButton1);
                            actionText1.setVisibility(View.VISIBLE);
                            actionText1.setText(action.get(0).getName());
                            setActionListener(actionText1, model, action.get(0), payload);
                        }

                        if (action.size() > 1 && action.get(1) != null) {
                            final TextView actionText2 = listItemLayout.findViewById(R.id.actionButton2);
                            View actionDivider2 = listItemLayout.findViewById(R.id.actionDivider2);
                            actionDivider2.setVisibility(View.VISIBLE);
                            actionText2.setVisibility(View.VISIBLE);
                            actionText2.setText(action.get(1).getName());
                            setActionListener(actionText2, model, action.get(1), payload);
                        }

                        if (action.size() > 2 && action.get(2) != null) {
                            final TextView actionText3 = listItemLayout.findViewById(R.id.actionButton3);
                            View actionDivider3 = listItemLayout.findViewById(R.id.actionDivider3);
                            actionDivider3.setVisibility(View.VISIBLE);
                            actionText3.setVisibility(View.VISIBLE);
                            actionText3.setText(action.get(2).getName());
                            setActionListener(actionText3, model, action.get(2), payload);
                        }
                    }
                }
            }
        }
    }
}
