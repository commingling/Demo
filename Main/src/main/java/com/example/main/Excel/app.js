const xlsx = require('xlsx');
const fs = require('fs');
const path = require('path');
const os = require('os');

function readTranslations(excelFilePath, keyColumn, translationColumns) {
    const workbook = xlsx.readFile(excelFilePath);
    const sheet = workbook.Sheets[workbook.SheetNames[3]]; // Assuming the translations are in the first sheet
    const data = xlsx.utils.sheet_to_json(sheet, { header: 1 });
    const translations = {};

    translationColumns.forEach(lang => {
        translations[lang.header] = {};
    });

    data.slice(1).forEach((row, rowIndex) => { // Skip header row
        const keyCell = row[keyColumn];
        if (keyCell !== undefined && keyCell !== null) {
            const key = keyCell.toString().trim();
            translationColumns.forEach(lang => {
                const valueCell = row[lang.index];
                if (valueCell !== undefined && valueCell !== null) {
                    translations[lang.header][key] = valueCell.toString().trim();
                }
            });
        } else {
            console.log(`Skipping row ${rowIndex + 2}: Key is undefined or null.`);
        }
    });

    return translations;
}

function updateJavaScriptFile(translations, outputPath) {
    Object.entries(translations).forEach(([lang, trans]) => {
        let content = 'export default {\n';
        Object.entries(trans).forEach(([key, value]) => {
            key = key.replace(/\s+/g, '_').replace(/"/g, '\\"');
            value = value.replace(/@/g, "{'@'}").replace(/"/g, '\\"').replace(/\n/g, '\\n');
            content += `  "${key}": "${value}",\n`;
        });
        content += '};\n';

        fs.writeFileSync(path.join(outputPath, `${lang}.js`), content, 'utf8');
        console.log(`Updated ${lang}.js`);
    });
}

// Correctly resolve the Excel file path
const homeDirectory = os.homedir();
const excelFilePath = path.join(homeDirectory, 'Desktop', '多语言初稿.xlsx');
const outputPath = path.join(__dirname, 'translations');

const keyColumn = 3;
const translationColumns = [
    { header: 'en', index: 3 },
    { header: 'zh_CN', index: 4 },
    { header: 'zh_TW', index: 5 },
    { header: 'es', index: 6 },
    { header: 'de', index: 7 },
    { header: 'no', index: 8 },
    { header: 'it', index: 9 },
    { header: 'ko', index: 10 },
    { header: 'fr', index: 11 },
];

const translations = readTranslations(excelFilePath, keyColumn, translationColumns);
updateJavaScriptFile(translations, outputPath);
