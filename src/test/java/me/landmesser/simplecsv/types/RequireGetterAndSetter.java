package me.landmesser.simplecsv.types;

import me.landmesser.simplecsv.CSVExportImportStrategy;
import me.landmesser.simplecsv.CSVInherit;
import me.landmesser.simplecsv.ExportImportStrategy;

@CSVInherit
@CSVExportImportStrategy(ExportImportStrategy.WITH_GETTERS_AND_SETTERS)
public class RequireGetterAndSetter extends NoGetterSetter {
}
