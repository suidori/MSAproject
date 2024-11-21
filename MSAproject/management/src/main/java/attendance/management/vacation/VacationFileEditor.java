package attendance.management.vacation;


import attendance.management.error.BizException;
import attendance.management.error.ErrorCode;
import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.object.bodytext.control.table.Cell;
import kr.dogfoot.hwplib.object.bodytext.paragraph.Paragraph;
import kr.dogfoot.hwplib.object.bodytext.paragraph.text.ParaText;
import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwplib.tool.objectfinder.CellFinder;
import kr.dogfoot.hwplib.writer.HWPWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class VacationFileEditor {

    private final VacationRepository vacationRepository;

    public String newHWP(long idx) throws Exception {
        HWPFile hwpFile = HWPReader.fromFile("vacation.hwp");

        String[] content = new String[]{"훈련과정명", "훈련기간", "성명", "주민등록번호", "사유", "희망일자", "비상연락망"};
        String[] body = new String[7];

        Vacation vacation = vacationRepository.findById(idx).orElseThrow(() -> new BizException(ErrorCode.REQUEST_NOT_FOUND));

        body[0] = vacation.getLecture().getTitle();
        body[1] = vacation.getLecture().getStartDate() + " ~ " + vacation.getLecture().getEndDate();
        body[2] = vacation.getUser().getName();
        body[3] = vacation.getPersonalNum();
        body[4] = vacation.getReason();
        body[5] = vacation.getDate().toString();
        body[6] = vacation.getPhonecall();


        for (int i = 0; i < 7; i++) {
            setCellTextByField(hwpFile, content[i], body[i]);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
        LocalDate now = LocalDate.now();
        String nowDate = now.format(formatter);

        int count = 1;

        File directory = new File("request_hwp" + File.separator + vacation.getLecture().getTitle());

        if(!directory.exists())
            directory.mkdirs();

        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().equals(nowDate + '_' + body[2] + "_휴가신청서(" + count + ").hwp")) {
                    count++;
                }
            }
        }

        String title = nowDate + '_' + body[2] + "_휴가신청서(" + count + ").hwp";

        HWPWriter.toFile(hwpFile, savePath(title, vacation.getLecture().getTitle()));

        vacation.setHwpfile(title);
        vacationRepository.save(vacation);

        return title;

    }

    private static void setCellTextByField(HWPFile hwpFile, String fieldName, String fieldText) throws UnsupportedEncodingException {
        ArrayList<Cell> cellList = CellFinder.findAll(hwpFile, fieldName);
        for (Cell c : cellList) {
            Paragraph firstPara = c.getParagraphList().getParagraph(0);
            ParaText paraText = firstPara.getText();
            if (paraText == null) {
                firstPara.createText();
                paraText = firstPara.getText();
            }

            paraText.addString(fieldText);
        }
    }

    private static String savePath(String filename, String lecturetitle) {
        return "request_hwp" + File.separator + lecturetitle + File.separator + filename;
    }

}
