package edu.iu.sci2.database.nsf.load.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.sci2.database.nsf.load.model.entity.Award;
import edu.iu.sci2.database.nsf.load.model.entity.Program;
import edu.iu.sci2.utilities.nsf.NsfDatabaseFieldNames;

public class ProgramReferenceCode extends RowItem<ProgramReferenceCode> {

	public static final Schema<ProgramReferenceCode> SCHEMA = new Schema<ProgramReferenceCode>(
			false,
			NsfDatabaseFieldNames.PROGRAM_REFERENCE_CODES_PROGRAM_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
			NsfDatabaseFieldNames.PROGRAM_REFERENCE_CODES_AWARD_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY
			).
			FOREIGN_KEYS(
				NsfDatabaseFieldNames.PROGRAM_REFERENCE_CODES_PROGRAM_FOREIGN_KEY,
					NsfDatabaseFieldNames.PROGRAM_TABLE_NAME,
				NsfDatabaseFieldNames.PROGRAM_REFERENCE_CODES_AWARD_FOREIGN_KEY,
					NsfDatabaseFieldNames.AWARD_TABLE_NAME
			);
	
	private Program program;
	private Award award;

	public ProgramReferenceCode(Program program, Award award) {
		super(createAttributes(program, award));
		this.program = program;
		this.award = award;
	}

	public Program getProgram() {
		return this.program;
	}
	
	public Award getAward() {
		return this.award;
	}

	/*@Override
	public Object createMergeKey() {
		List<Object> mergeKey = new ArrayList<Object>();
		mergeKey.add(this.program.getPrimaryKey());
		mergeKey.add(this.award.getPrimaryKey());

		return mergeKey;
	}

	@Override
	public void merge(ProgramReferenceCode otherItem) {
	}*/

	private static Dictionary<String, Object> createAttributes(Program program, Award award) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(
			NsfDatabaseFieldNames.PROGRAM_REFERENCE_CODES_AWARD_FOREIGN_KEY,
			award.getPrimaryKey());
		attributes.put(
			NsfDatabaseFieldNames.PROGRAM_REFERENCE_CODES_PROGRAM_FOREIGN_KEY,
			program.getPrimaryKey());

		return attributes;
	}
}