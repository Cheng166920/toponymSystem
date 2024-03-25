package com.example.toponym.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.List;

import cn.hutool.core.io.resource.ClassPathResource;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by wangpeng on 2018/02/01.
 */
public class PdfUtils {

    // 利用模板生成pdf
    public static void pdfout(String templatePath,Map<String,String> data,OutputStream out) {
        // 模板路径
        ClassPathResource readFile = new ClassPathResource(templatePath);
        byte[] bytes = readFile.readBytes();
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            BaseFont bf = BaseFont.createFont("/static/register/simsun.ttc,0",BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
            reader = new PdfReader(bytes);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();

            form.addSubstitutionFont(bf);
            for(String key : data.keySet()){
                String value = data.get(key).toString();
                form.setFieldProperty(key,"textsize",10f,null);
                form.setField(key,value);

            }
            stamper.setFormFlattening(true);// 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
            stamper.close();
            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            for(int i = 1; i <=reader.getNumberOfPages();i ++){
                PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), i);
                copy.addPage(importPage);
            }
            doc.close();

        } catch (IOException e) {
            System.out.println(e);
        } catch (DocumentException e) {
            System.out.println(e);
        }

    }

    public static byte[] toponymPdfout(String templatePath, Map<String, String> data, OutputStream out) {
        // 模板路径
        ClassPathResource readFile = new ClassPathResource(templatePath);
        byte[] bytes = readFile.readBytes();
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            BaseFont bf = BaseFont.createFont("/static/register/simsun.ttc,0",BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
            Font textFont = new Font(bf, 10, Font.NORMAL);
            reader = new PdfReader(bytes);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            List<AcroFields.FieldPosition> multiLinePosition = new ArrayList<>();
            form.addSubstitutionFont(bf);
            for(String key : data.keySet()){
                String value = data.get(key);
                multiLinePosition = form.getFieldPositions(key);
                if(multiLinePosition != null) {
                    int page = multiLinePosition.get(0).page;
                    Rectangle rectangle = multiLinePosition.get(0).position;
                    float left = rectangle.getLeft();
                    float right = rectangle.getRight();
                    float top = rectangle.getTop();
                    float bottom = rectangle.getBottom();
                    PdfContentByte pdfContentByte = stamper.getOverContent(page);
                    ColumnText columnText = new ColumnText(pdfContentByte);
                    Rectangle r = new Rectangle(left, bottom, right, top);
                    columnText.setSimpleColumn(r);
                    Chunk chunk = new Chunk(value);
                    Paragraph paragraph = new Paragraph(11,chunk);
                    columnText.addText(paragraph);
                    paragraph.setAlignment(Element.ALIGN_CENTER);
                    paragraph.setFont(textFont);
                    columnText.addElement(paragraph);
                    columnText.go();
                }
            }
            stamper.setFormFlattening(true);// 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
            byte[] newBytes = bos.toByteArray();
            stamper.close();
            Document doc = new Document();
            PdfWriter writer = PdfWriter.getInstance(doc, out);
            doc.open();
            PdfContentByte cbUnder = writer.getDirectContentUnder();
            PdfImportedPage importPage = writer.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            cbUnder.addTemplate(importPage,0,0);
            doc.newPage();
            createBlankTable(writer, doc, bf, 36);
            createTable(writer, doc, data);
            doc.close();
            return newBytes;
        } catch (IOException e) {
            System.out.println(e);
        } catch (DocumentException e) {
            System.out.println(e);
        }
        return null;

    }

    //为一个表格添加内容
    private static PdfPCell createSetCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Phrase(value, font));
        cell.setMinimumHeight(17);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }


    //添加表格
    public static void createTable(PdfWriter writer, Document document,Map<String,String> map) throws DocumentException, IOException {

        PdfPTable table = new PdfPTable(new float[]{36, 177});
        table.setTotalWidth(426);

        table.setLockedWidth(true);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);//居中
        table.writeSelectedRows(0, -1, 500, 800, writer.getDirectContentUnder());

        //定义数据的字体
        BaseFont baseFont = BaseFont.createFont("/static/register/simsun.ttc,0",BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        Font textFont = new Font(baseFont, 10, Font.NORMAL);


        PdfPCell setCell1 = createSetCell("地理实体概况", textFont);
        setCell1.setMinimumHeight(100);
        PdfPCell setCell2 = createSetCell(map.get("ENTITY_OVERVIEW"), textFont);
        setCell2.setVerticalAlignment(PdfPCell.ALIGN_CENTER);//设置单元格的垂直对齐方式
        setCell2.setPaddingTop(8);
        table.addCell(setCell1);
        table.addCell(setCell2);

        PdfPCell setCell3 = createSetCell("多媒体信息", textFont);
        PdfPCell setCell4 = createSetCell(map.get("MULTIMEDIA_INFORMATION"), textFont);
        table.addCell(setCell3);
        table.addCell(setCell4);

        PdfPCell setCell5 = createSetCell("资料来源", textFont);
        PdfPCell setCell6 = createSetCell(map.get("SOURCE"), textFont);
        table.addCell(setCell5);
        table.addCell(setCell6);

        //列表数据
        JSONObject obj =  JSONObject.parseObject(map.get("SPECIAL_INFORMATION"));
        for(Map.Entry<String, Object> entry : obj.entrySet()){
            String k = entry.getKey();
            String v = entry.getValue().toString();
            PdfPCell setCell_1 = createSetCell(k, textFont);
            PdfPCell setCell_2 = createSetCell(v, textFont);
            table.addCell(setCell_1);
            table.addCell(setCell_2);
        }

        PdfPCell setCell7 = createSetCell("其他信息", textFont);
        PdfPCell setCell8 = createSetCell(map.get("ADDITIONAL_INFORMATION"), textFont);
        table.addCell(setCell7);
        table.addCell(setCell8);

        PdfPCell setCell9 = createSetCell("备注", textFont);
        PdfPCell setCell10 = createSetCell(map.get("REMARK"), textFont);
        table.addCell(setCell9);
        table.addCell(setCell10);
        document.add(table);
    }

    /**
     * 创建表格跟表格之间的空白间隔
     */
    public static void createBlankTable(PdfWriter writer, Document document, BaseFont font, int height) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{30});
        table.setTotalWidth(520);
      //  table.setPaddingTop(500);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);//居中
        table.writeSelectedRows(0, -1, 500, 800, writer.getDirectContentUnder());
        Font textFont = new Font(font, 10, Font.NORMAL);
        PdfPCell cell = new PdfPCell(new Paragraph(" ", textFont));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(height);
        cell.setColspan(1);
        table.addCell(cell);
        document.add(table);
    }


}
