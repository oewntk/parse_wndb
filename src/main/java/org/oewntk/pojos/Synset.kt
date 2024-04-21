/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.pojos

import org.oewntk.pojos.RelationType.Companion.parseRelationType

/**
 * Synset, a core synset extended to include relations and possibly verb frames
 *
 * @param synsetId   synset id
 * @param lemmas     lemmas
 * @param type       type
 * @param domain     lex domain
 * @param gloss      gloss
 * @param relations  relations
 * @param verbFrames verb frames
 *
 * @author Bernard Bou
 */
class Synset private constructor(
	synsetId: SynsetId,
	lemmas: Array<LemmaCS>,
	type: Type,
	domain: Domain,
	gloss: Gloss,
	relations: Array<Relation>,
	verbFrames: Array<VerbFrameRef>?
) : CoreSynset(synsetId, lemmas, type, domain, gloss) {

	/**
	 * Get relations
	 *
	 * @return relations
	 */
	@JvmField
	val relations: Array<Relation>?

	/**
	 * Get verb frames
	 *
	 * @return verb frames
	 */
	val verbFrames: Array<VerbFrameRef>?

	/**
	 * Constructor
	 */
	init {
		this.relations = relations
		this.verbFrames = verbFrames
	}

	override fun toString(): String {
		val sb = StringBuilder()
		sb.append(super.toString())
		if (this.relations != null) {
			sb.append(" relations={")
			for (i in relations.indices) {
				if (i != 0) {
					sb.append(",")
				}
				sb.append(relations[i].toString())
			}
			sb.append("}")
		}
		if (this.verbFrames != null) {
			sb.append(" frames={")
			for (i in verbFrames.indices) {
				if (i != 0) {
					sb.append(",")
				}
				sb.append(verbFrames[i].toString())
			}
			sb.append("}")
		}
		return sb.toString()
	}

	/**
	 * Pretty string
	 *
	 * @return pretty string
	 */
	fun toPrettyString(): String {
		val sb = StringBuilder()
		sb.append(super.toString())
		sb.append('\n')
		sb.append(gloss.toPrettyString())

		if (this.relations != null) {
			sb.append('\n')
			sb.append("relations={")
			sb.append('\n')
			for (i in relations.indices) {
				if (i != 0) {
					sb.append(",")
					sb.append('\n')
				}
				sb.append('\t')
				sb.append(relations[i].toString())
			}
			sb.append('\n')
			sb.append("}")
		}
		if (this.verbFrames != null) {
			sb.append('\n')
			sb.append("frames={")
			for (i in verbFrames.indices) {
				if (i != 0) {
					sb.append(",")
				}
				sb.append(verbFrames[i].toString())
			}
			sb.append("}")
		}
		return sb.toString()
	}

	companion object {
		/**
		 * Parse from line
		 *
		 * @param line  line
		 * @param isAdj whether adj synsets are being parsed
		 * @return synset
		 * @throws ParsePojoException parse exception
		 */
		@JvmStatic
		@Throws(ParsePojoException::class)
		fun parseSynsetLine(line: String, isAdj: Boolean): Synset {
			try {
				// core subparse
				val protoSynset = parseCoreSynset(line, isAdj)

				// copy data from proto
				val type = protoSynset.type
				val csLemmas = protoSynset.cSLemmas
				val synsetId = protoSynset.id
				val domain = protoSynset.domain
				val gloss = protoSynset.gloss

				// split into fields
				val fields = line.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
				var fieldPointer = 0

				// data
				val relations: Array<Relation>
				var frames: Array<VerbFrameRef>? = null

				// offset
				fieldPointer++

				// domain
				fieldPointer++

				// part-of-speech
				fieldPointer++

				// lemma count
				fieldPointer++

				// lemma set
				fieldPointer += 2 * csLemmas.size

				// relation count
				val relationCount = fields[fieldPointer].toInt(10)
				fieldPointer++

				// relations
				relations = Array(relationCount) {

					// read data
					val relationTypeField = fields[fieldPointer++]
					val relationSynsetIdField = fields[fieldPointer++]
					val relationPosField = fields[fieldPointer++]
					val relationSourceTargetField = fields[fieldPointer++]
					val relationSynsetId = relationSynsetIdField.toLong()
					val relationSourceTarget = relationSourceTargetField.toInt(16)

					// compute
					val synsetType = Type.parseType(relationPosField[0])
					val relationType = parseRelationType(relationTypeField)
					val toId = SynsetId(synsetType.toPos(), relationSynsetId)

					// create
					if (relationSourceTarget != 0) {
						val fromWordIndex = relationSourceTarget shr 8
						val toWordIndex = relationSourceTarget and 0xff
						val fromLemma = csLemmas[fromWordIndex - 1]
						val toLemma = LemmaRef(toId, toWordIndex)
						LexRelation(relationType, synsetId, toId, fromLemma, toLemma)
					} else {
						Relation(relationType, synsetId, toId)
					}
				}

				// frames
				if (type.toChar() == 'v' && fields[fieldPointer] != "|") {
					// frame count
					val frameCount = fields[fieldPointer].toInt(10)
					fieldPointer++

					// frames
					frames = Array(frameCount) {
						// read data
						fieldPointer++ // '+'
						val frameIdField = fields[fieldPointer++]
						val wordIndexField = fields[fieldPointer++]

						// compute
						val frameId = frameIdField.toInt()
						val wordIndex = wordIndexField.toInt(16)

						// create
						val frameLemmas = if (wordIndex != 0) {
							arrayOf(csLemmas[wordIndex - 1].lemma)
						} else  // 0 means all
						{
							protoSynset.lemmas
						}
						VerbFrameRef(frameLemmas, frameId)
					}
				}
				return Synset(synsetId, csLemmas, type, domain, gloss, relations, frames)
			} catch (e: Exception) {
				throw ParsePojoException(e)
			}
		}
	}
}