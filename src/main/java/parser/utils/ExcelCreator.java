package parser.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class ExcelCreator {

    private final HSSFWorkbook book;

    private ExcelCreator() {
        this.book = initialize();
    }

    public static HSSFWorkbook build(List<Map<String, String>> values) {
        ExcelCreator excelCreator = new ExcelCreator();
        excelCreator.setValues(values);

        HSSFSheet hssfSheet = excelCreator.book.getSheetAt(0);
        hssfSheet.autoSizeColumn(0, true);
        hssfSheet.autoSizeColumn(1, true);
        hssfSheet.autoSizeColumn(2, true);

        return excelCreator.book;
    }

    private HSSFWorkbook initialize() {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet();
        HSSFRow hssfRow = hssfSheet.createRow(0);

        HSSFCell name = hssfRow.createCell(0);
        HSSFCell description = hssfRow.createCell(1);
        HSSFCell category = hssfRow.createCell(2);
        HSSFCell address = hssfRow.createCell(3);

        name.setCellValue("Имя");
        description.setCellValue("Описание");
        category.setCellValue("Категория");
        address.setCellValue("Адресс");

        return hssfWorkbook;
    }

    private void setValues(List<Map<String, String>> values) {
        AtomicInteger index = new AtomicInteger(1);
        values.forEach(value -> {
            if (value.size() == 0)
                return;

            HSSFRow hssfRow = book.getSheetAt(0).createRow(index.getAndIncrement());

            HSSFCell name = hssfRow.createCell(0);
            HSSFCell description = hssfRow.createCell(1);
            HSSFCell category = hssfRow.createCell(2);
            HSSFCell address = hssfRow.createCell(3);

            String nameValue = value.get("search-business-snippet-view__title");
            if (Objects.nonNull(nameValue) && !nameValue.isEmpty())
                name.setCellValue(nameValue);

            String descriptionValueOne = value.get("search-business-snippet-subtitle-view__title");
            String descriptionValueTwo = value.get("search-business-snippet-subtitle-view__description");
            if (Objects.nonNull(descriptionValueOne) && !descriptionValueOne.isEmpty() && Objects.nonNull(descriptionValueTwo) && !descriptionValueTwo.isEmpty())
                description.setCellValue(descriptionValueOne + "  " + descriptionValueTwo);

            String categoryValue = value.get("search-business-snippet-view__category");
            if (Objects.nonNull(categoryValue) && !categoryValue.isEmpty())
                category.setCellValue(categoryValue);

            String addressValue = value.get("search-business-snippet-view__address");
            if (Objects.nonNull(addressValue) && !addressValue.isEmpty())
                address.setCellValue(addressValue);

            if (isEmptyCell(name) && isEmptyCell(address) && isEmptyCell(category) && isEmptyCell(description)) {
                book.getSheetAt(0).removeRow(hssfRow);
                index.decrementAndGet();
            }
        });
    }

    private boolean isEmptyCell(HSSFCell cell) {
        String value = cell.getStringCellValue();
        return Objects.isNull(value) || value.isEmpty();
    }
}
