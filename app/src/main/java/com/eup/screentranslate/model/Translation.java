package com.eup.screentranslate.model;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Translation {
    @SerializedName("sentences")
    @Expose
    private List<Sentence> sentences = null;
    @SerializedName("dict")
    @Expose
    private List<Dict> dict = null;
    @SerializedName("src")
    @Expose
    private String src;
    @SerializedName("alternative_translations")
    @Expose
    private List<AlternativeTranslation> alternativeTranslations = null;
    @SerializedName("confidence")
    @Expose
    private Double confidence;
    @SerializedName("spell")
    @Expose
    private Spell spell;
    @SerializedName("ld_result")
    @Expose
    private LdResult ldResult;
    @SerializedName("synsets")
    @Expose
    private List<Synset> synsets = null;
    @SerializedName("definitions")
    @Expose
    private List<Definition> definitions = null;
    @SerializedName("examples")
    @Expose
    private Examples examples;

    public void convertMean(){
        if (sentences == null){
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer1 = new StringBuffer();
        for (Translation.Sentence sentence : sentences) {
            if (null!=sentence && !sentence.getOrig().isEmpty())
                stringBuffer.append(sentence.getTrans());
            if (null!= sentence.getSrcTranslit() && !sentence.getSrcTranslit().isEmpty())
                stringBuffer1.append(sentence.getSrcTranslit());
        }
        String mean = stringBuffer.toString();
        String romaji = stringBuffer1.toString();
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    public List<Dict> getDict() {
        return dict;
    }

    public void setDict(List<Dict> dict) {
        this.dict = dict;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public List<AlternativeTranslation> getAlternativeTranslations() {
        return alternativeTranslations;
    }

    public void setAlternativeTranslations(List<AlternativeTranslation> alternativeTranslations) {
        this.alternativeTranslations = alternativeTranslations;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public Spell getSpell() {
        return spell;
    }

    public void setSpell(Spell spell) {
        this.spell = spell;
    }

    public LdResult getLdResult() {
        return ldResult;
    }

    public void setLdResult(LdResult ldResult) {
        this.ldResult = ldResult;
    }

    public List<Synset> getSynsets() {
        return synsets;
    }

    public void setSynsets(List<Synset> synsets) {
        this.synsets = synsets;
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
    }

    public Examples getExamples() {
        return examples;
    }

    public void setExamples(Examples examples) {
        this.examples = examples;
    }

    public String getTranslateContent(){
        if (sentences == null || sentences.isEmpty()) return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (Sentence sentence : sentences){
            if (sentence.orig != null && !sentence.orig.isEmpty()){
                stringBuilder.append(sentence.trans);
            }
        }

        return stringBuilder.toString();
    }


    public class Examples {

        @SerializedName("example")
        @Expose
        private List<Example> example = null;

        public List<Example> getExample() {
            return example;
        }

        public void setExample(List<Example> example) {
            this.example = example;
        }

    }


     public class Example {

        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("source_type")
        @Expose
        private Integer sourceType;
        @SerializedName("definition_id")
        @Expose
        private String definitionId;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Integer getSourceType() {
            return sourceType;
        }

        public void setSourceType(Integer sourceType) {
            this.sourceType = sourceType;
        }

        public String getDefinitionId() {
            return definitionId;
        }

        public void setDefinitionId(String definitionId) {
            this.definitionId = definitionId;
        }
    }

    public class LdResult {

        @SerializedName("srclangs")
        @Expose
        private List<String> srclangs = null;
        @SerializedName("srclangs_confidences")
        @Expose
        private List<Double> srclangsConfidences = null;
        @SerializedName("extended_srclangs")
        @Expose
        private List<String> extendedSrclangs = null;

        public List<String> getSrclangs() {
            return srclangs;
        }

        public void setSrclangs(List<String> srclangs) {
            this.srclangs = srclangs;
        }

        public List<Double> getSrclangsConfidences() {
            return srclangsConfidences;
        }

        public void setSrclangsConfidences(List<Double> srclangsConfidences) {
            this.srclangsConfidences = srclangsConfidences;
        }

        public List<String> getExtendedSrclangs() {
            return extendedSrclangs;
        }

        public void setExtendedSrclangs(List<String> extendedSrclangs) {
            this.extendedSrclangs = extendedSrclangs;
        }

    }

    public static class Sentence {

        @SerializedName("trans")
        @Expose
        private String trans;
        @SerializedName("orig")
        @Expose
        private String orig;
        @SerializedName("backend")
        @Expose
        private Integer backend;
        @SerializedName("src_translit")
        @Expose
        private String srcTranslit;

        public String getTrans() {
            return trans;
        }

        public void setTrans(String trans) {
            this.trans = trans;
        }

        public String getOrig() {
            return orig;
        }

        public void setOrig(String orig) {
            this.orig = orig;
        }

        public Integer getBackend() {
            return backend;
        }

        public void setBackend(Integer backend) {
            this.backend = backend;
        }

        public String getSrcTranslit() {
            return srcTranslit;
        }

        public void setSrcTranslit(String srcTranslit) {
            this.srcTranslit = srcTranslit;
        }
    }

    public class Spell {

    }

    public class Dict {

        @SerializedName("pos")
        @Expose
        private String pos;
        @SerializedName("terms")
        @Expose
        private List<String> terms = null;
        @SerializedName("entry")
        @Expose
        private List<Entry> entry = null;
        @SerializedName("base_form")
        @Expose
        private String baseForm;
        @SerializedName("pos_enum")
        @Expose
        private Integer posEnum;

        public String getPos() {
            return pos;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }

        public List<String> getTerms() {
            return terms;
        }

        public void setTerms(List<String> terms) {
            this.terms = terms;
        }

        public List<Entry> getEntry() {
            return entry;
        }

        public void setEntry(List<Entry> entry) {
            this.entry = entry;
        }

        public String getBaseForm() {
            return baseForm;
        }

        public void setBaseForm(String baseForm) {
            this.baseForm = baseForm;
        }

        public Integer getPosEnum() {
            return posEnum;
        }

        public void setPosEnum(Integer posEnum) {
            this.posEnum = posEnum;
        }

    }

    public static class Entry {

        @SerializedName("word")
        @Expose
        private String word;
        @SerializedName("reverse_translation")
        @Expose
        private List<String> reverseTranslation = null;

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public List<String> getReverseTranslation() {
            return reverseTranslation;
        }

        public void setReverseTranslation(List<String> reverseTranslation) {
            this.reverseTranslation = reverseTranslation;
        }

    }

    public class AlternativeTranslation {

        @SerializedName("src_phrase")
        @Expose
        private String srcPhrase;
        @SerializedName("alternative")
        @Expose
        private List<Alternative> alternative = null;
        @SerializedName("srcunicodeoffsets")
        @Expose
        private List<Srcunicodeoffset> srcunicodeoffsets = null;
        @SerializedName("raw_src_segment")
        @Expose
        private String rawSrcSegment;
        @SerializedName("start_pos")
        @Expose
        private Integer startPos;
        @SerializedName("end_pos")
        @Expose
        private Integer endPos;

        public String getSrcPhrase() {
            return srcPhrase;
        }

        public void setSrcPhrase(String srcPhrase) {
            this.srcPhrase = srcPhrase;
        }

        public List<Alternative> getAlternative() {
            return alternative;
        }

        public void setAlternative(List<Alternative> alternative) {
            this.alternative = alternative;
        }

        public List<Srcunicodeoffset> getSrcunicodeoffsets() {
            return srcunicodeoffsets;
        }

        public void setSrcunicodeoffsets(List<Srcunicodeoffset> srcunicodeoffsets) {
            this.srcunicodeoffsets = srcunicodeoffsets;
        }

        public String getRawSrcSegment() {
            return rawSrcSegment;
        }

        public void setRawSrcSegment(String rawSrcSegment) {
            this.rawSrcSegment = rawSrcSegment;
        }

        public Integer getStartPos() {
            return startPos;
        }

        public void setStartPos(Integer startPos) {
            this.startPos = startPos;
        }

        public Integer getEndPos() {
            return endPos;
        }

        public void setEndPos(Integer endPos) {
            this.endPos = endPos;
        }

    }


    public class Alternative {

        @SerializedName("word_postproc")
        @Expose
        private String wordPostproc;
        @SerializedName("score")
        @Expose
        private Integer score;
        @SerializedName("has_preceding_space")
        @Expose
        private Boolean hasPrecedingSpace;
        @SerializedName("attach_to_next_token")
        @Expose
        private Boolean attachToNextToken;

        public String getWordPostproc() {
            return wordPostproc;
        }

        public void setWordPostproc(String wordPostproc) {
            this.wordPostproc = wordPostproc;
        }

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }

        public Boolean getHasPrecedingSpace() {
            return hasPrecedingSpace;
        }

        public void setHasPrecedingSpace(Boolean hasPrecedingSpace) {
            this.hasPrecedingSpace = hasPrecedingSpace;
        }

        public Boolean getAttachToNextToken() {
            return attachToNextToken;
        }

        public void setAttachToNextToken(Boolean attachToNextToken) {
            this.attachToNextToken = attachToNextToken;
        }

    }

    public class Definition {

        @SerializedName("pos")
        @Expose
        private String pos;
        @SerializedName("entry")
        @Expose
        private List<Entry__> entry = null;
        @SerializedName("base_form")
        @Expose
        private String baseForm;

        public String getPos() {
            return pos;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }

        public List<Entry__> getEntry() {
            return entry;
        }

        public void setEntry(List<Entry__> entry) {
            this.entry = entry;
        }

        public String getBaseForm() {
            return baseForm;
        }

        public void setBaseForm(String baseForm) {
            this.baseForm = baseForm;
        }

    }

    public class Entry_ {

        @SerializedName("synonym")
        @Expose
        private List<String> synonym = null;
        @SerializedName("definition_id")
        @Expose
        private String definitionId;

        public List<String> getSynonym() {
            return synonym;
        }

        public void setSynonym(List<String> synonym) {
            this.synonym = synonym;
        }

        public String getDefinitionId() {
            return definitionId;
        }

        public void setDefinitionId(String definitionId) {
            this.definitionId = definitionId;
        }

    }

    public static class Entry__ {

        @SerializedName("gloss")
        @Expose
        private String gloss;
        @SerializedName("definition_id")
        @Expose
        private String definitionId;
        @SerializedName("example")
        @Expose
        private String example;

        public String getGloss() {
            return gloss;
        }

        public void setGloss(String gloss) {
            this.gloss = gloss;
        }

        public String getDefinitionId() {
            return definitionId;
        }

        public void setDefinitionId(String definitionId) {
            this.definitionId = definitionId;
        }

        public String getExample() {
            return example;
        }

        public void setExample(String example) {
            this.example = example;
        }


    }

    public class Synset {

        @SerializedName("pos")
        @Expose
        private String pos;
        @SerializedName("entry")
        @Expose
        private List<Entry_> entry = null;
        @SerializedName("base_form")
        @Expose
        private String baseForm;

        public String getPos() {
            return pos;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }

        public List<Entry_> getEntry() {
            return entry;
        }

        public void setEntry(List<Entry_> entry) {
            this.entry = entry;
        }

        public String getBaseForm() {
            return baseForm;
        }

        public void setBaseForm(String baseForm) {
            this.baseForm = baseForm;
        }

    }


    public class Srcunicodeoffset {

        @SerializedName("begin")
        @Expose
        private Integer begin;
        @SerializedName("end")
        @Expose
        private Integer end;

        public Integer getBegin() {
            return begin;
        }

        public void setBegin(Integer begin) {
            this.begin = begin;
        }

        public Integer getEnd() {
            return end;
        }

        public void setEnd(Integer end) {
            this.end = end;
        }

    }


}
