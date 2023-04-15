package com.vaadin.demo.gpt;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.textfield.TextField;

public class MoneyField extends CustomField<Double> {

    private final TextField textField;

    public MoneyField(String caption) {
        setCaption(caption);

        textField = new TextField();
        textField.setPlaceholder("0.00");
        textField.setPattern("^[0-9]+(\\.[0-9]{1,2})?$"); // регулярное выражение для валидации

        // Добавляем валидацию на каждое изменение поля
        textField.addValueChangeListener(e -> {
            String value = e.getValue();
            if (value != null && !value.isEmpty()) {
                try {
                    double amount = Double.parseDouble(value);
                    setValue(amount);
                    textField.setComponentError(null); // сброс ошибки валидации, если она была
                } catch (NumberFormatException ex) {
                    setInvalidInputError();
                }
            } else {
                setValue(null);
                textField.setComponentError(null);
            }
        });

        // Добавляем обработчик потери фокуса
        textField.addBlurListener(e -> {
            if (getValue() == null) {
                setInvalidInputError();
            }
        });

        // Добавляем текстовое поле в кастомный компонент
        setCompositionRoot(textField);
    }

    private void setInvalidInputError() {
        textField.setComponentError(new UserError("Неверный формат числа. Правильный формат: 100.00"));
    }

    @Override
    protected Double generateModelValue() {
        return getValue();
    }

    @Override
    protected void setPresentationValue(Double newPresentationValue) {
        textField.setValue(newPresentationValue == null ? "" : String.format("%.2f", newPresentationValue));
    }
}