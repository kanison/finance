package com.tenpay.sm.common.lang;

import java.io.File;

import java.net.URL;

import java.util.StringTokenizer;

/**
 * �����ļ��Ĺ����ࡣ
 *
 * @author Michael Zhou
 * @version $Id: FileUtil.java 900 2004-04-19 01:15:33Z baobao $
 */
public class FileUtil {
    /* ============================================================================ */
    /*  ������singleton��                                                           */
    /* ============================================================================ */
    private static final char   COLON_CHAR     = ':';
    private static final String UNC_PREFIX     = "//";
    private static final String SLASH          = "/";
    private static final String BACKSLASH      = "\\";
    private static final char   SLASH_CHAR     = '/';
    private static final char   BACKSLASH_CHAR = '\\';
    private static final String ALL_SLASH      = "/\\";

    /** ��׺�ָ����� */
    public static final String EXTENSION_SEPARATOR = ".";

    /** ��ǰĿ¼�Ǻţ�"." */
    public static final String CURRENT_DIR = ".";

    /** �ϼ�Ŀ¼�Ǻţ�".." */
    public static final String UP_LEVEL_DIR = "..";

    /* ============================================================================ */
    /*  ���·����                                                                */
    /*                                                                              */
    /*  ȥ��'.'��'..'��֧��windows·����UNC·����                                   */
    /* ============================================================================ */

    /**
     * ���·����
     * 
     * <p>
     * �÷������Բ���ϵͳ�����ͣ����Ƿ����ԡ�<code>/</code>����ʼ�ľ���·����ת���������£�
     * 
     * <ol>
     * <li>
     * ·��Ϊ<code>null</code>���򷵻�<code>null</code>��
     * </li>
     * <li>
     * ������backslash("\\")ת����slash("/")��
     * </li>
     * <li>
     * ȥ���ظ���"/"��"\\"��
     * </li>
     * <li>
     * ȥ��"."���������".."��������˷һ��Ŀ¼��
     * </li>
     * <li>
     * ��·������"/"��
     * </li>
     * <li>
     * ����·��ĩβ��"/"������еĻ�����
     * </li>
     * <li>
     * ���ھ���·�������".."��˷��·�������˸�Ŀ¼�������Ƿ�·��������<code>null</code>��
     * </li>
     * </ol>
     * </p>
     *
     * @param path Ҫ��񻯵�·��
     *
     * @return ��񻯺��·�������·���Ƿ����򷵻�<code>null</code>
     */
    public static String normalizeAbsolutePath(String path) {
        String normalizedPath = normalizePath(path, false);

        if ((normalizedPath != null) && !normalizedPath.startsWith(SLASH)) {
            if (normalizedPath.equals(CURRENT_DIR)
                        || normalizedPath.equals(CURRENT_DIR + SLASH_CHAR)) {
                normalizedPath = SLASH;
            } else if (normalizedPath.startsWith(UP_LEVEL_DIR)) {
                normalizedPath = null;
            } else {
                normalizedPath = SLASH_CHAR + normalizedPath;
            }
        }

        return normalizedPath;
    }

    /**
     * ���·����
     * 
     * <p>
     * �÷����Զ��б����ϵͳ�����͡�ת���������£�
     * 
     * <ol>
     * <li>
     * ·��Ϊ<code>null</code>���򷵻�<code>null</code>��
     * </li>
     * <li>
     * ������backslash("\\")ת����slash("/")��
     * </li>
     * <li>
     * ȥ���ظ���"/"��"\\"��
     * </li>
     * <li>
     * ȥ��"."���������".."��������˷һ��Ŀ¼��
     * </li>
     * <li>
     * �վ���·������"/"�������·������"./"��
     * </li>
     * <li>
     * ����·��ĩβ��"/"������еĻ�����
     * </li>
     * <li>
     * ���ھ���·�������".."��˷��·�������˸�Ŀ¼�������Ƿ�·��������<code>null</code>��
     * </li>
     * <li>
     * ����Windowsϵͳ����Щ·���������ǰ׺������������"c:"��UNC��"//hostname"��������Щ·����������ǰ׺����������·�����������������й���
     * </li>
     * <li>
     * Windows����������ת���ɴ�д����"c:"ת����"C:"��
     * </li>
     * </ol>
     * </p>
     *
     * @param path Ҫ��񻯵�·��
     *
     * @return ��񻯺��·�������·���Ƿ����򷵻�<code>null</code>
     */
    public static String normalizePath(String path) {
        return normalizePath(path, SystemUtil.getOsInfo().isWindows());
    }

    /**
     * ���·�����������£�
     * 
     * <ol>
     * <li>
     * ·��Ϊ<code>null</code>���򷵻�<code>null</code>��
     * </li>
     * <li>
     * ������backslash("\\")ת����slash("/")��
     * </li>
     * <li>
     * ȥ���ظ���"/"��"\\"��
     * </li>
     * <li>
     * ȥ��"."���������".."��������˷һ��Ŀ¼��
     * </li>
     * <li>
     * �վ���·������"/"�������·������"./"��
     * </li>
     * <li>
     * ����·��ĩβ��"/"������еĻ�����
     * </li>
     * <li>
     * ���ھ���·�������".."��˷��·�������˸�Ŀ¼�������Ƿ�·��������<code>null</code>��
     * </li>
     * <li>
     * ����Windowsϵͳ����Щ·���������ǰ׺������������"c:"��UNC��"//hostname"��������Щ·����������ǰ׺����������·�����������������й���
     * </li>
     * <li>
     * Windows����������ת���ɴ�д����"c:"ת����"C:"��
     * </li>
     * </ol>
     * 
     *
     * @param path Ҫ��񻯵�·��
     *
     * @return ��񻯺��·�������·���Ƿ����򷵻�<code>null</code>
     */
    public static String normalizeWindowsPath(String path) {
        return normalizePath(path, true);
    }

    /**
     * ���Unix����·������֧��Windows����������UNC·����
     * 
     * <p>
     * ת���������£�
     * 
     * <ol>
     * <li>
     * ·��Ϊ<code>null</code>���򷵻�<code>null</code>��
     * </li>
     * <li>
     * ������backslash("\\")ת����slash("/")��
     * </li>
     * <li>
     * ȥ���ظ���"/"��"\\"��
     * </li>
     * <li>
     * ȥ��"."���������".."��������˷һ��Ŀ¼��
     * </li>
     * <li>
     * �վ���·������"/"�������·������"./"��
     * </li>
     * <li>
     * ����·��ĩβ��"/"������еĻ�����
     * </li>
     * <li>
     * ���ھ���·�������".."��˷��·�������˸�Ŀ¼�������Ƿ�·��������<code>null</code>��
     * </li>
     * </ol>
     * </p>
     *
     * @param path Ҫ��񻯵�·��
     *
     * @return ��񻯺��·�������·���Ƿ����򷵻�<code>null</code>
     */
    public static String normalizeUnixPath(String path) {
        return normalizePath(path, false);
    }

    /**
     * ���·�����������£�
     * 
     * <ol>
     * <li>
     * ·��Ϊ<code>null</code>���򷵻�<code>null</code>��
     * </li>
     * <li>
     * ������backslash("\\")ת����slash("/")��
     * </li>
     * <li>
     * ȥ���ظ���"/"��"\\"��
     * </li>
     * <li>
     * ȥ��"."���������".."��������˷һ��Ŀ¼��
     * </li>
     * <li>
     * �վ���·������"/"�������·������"./"��
     * </li>
     * <li>
     * ����·��ĩβ��"/"������еĻ�����
     * </li>
     * <li>
     * ���ھ���·�������".."��˷��·�������˸�Ŀ¼�������Ƿ�·��������<code>null</code>��
     * </li>
     * <li>
     * ����Windowsϵͳ����Щ·���������ǰ׺������������"c:"��UNC��"//hostname"��������Щ·����������ǰ׺����������·�����������������й���
     * </li>
     * <li>
     * Windows����������ת���ɴ�д����"c:"ת����"C:"��
     * </li>
     * </ol>
     * 
     *
     * @param path Ҫ��񻯵�·��
     * @param isWindows �Ƿ���windows·�������Ϊ<code>true</code>����֧������������UNC·��
     *
     * @return ��񻯺��·�������·���Ƿ����򷵻�<code>null</code>
     */
    private static String normalizePath(String path, boolean isWindows) {
        if (path == null) {
            return null;
        }

        path     = path.trim();

        // ��"\\"ת����"/"���Ա�ͳһ����
        path = path.replace(BACKSLASH_CHAR, SLASH_CHAR);

        // ȡ��ϵͳ�ض���·��ǰ׺������windowsϵͳ�������ǣ�"C:"����"//hostname"
        String prefix = getSystemDependentPrefix(path, isWindows);

        if (prefix == null) {
            return null;
        }

        path = path.substring(prefix.length());

        // ���ھ���·����prefix������"/"��β����֮������·����prefix.length > 0
        if ((prefix.length() > 0) || path.startsWith(SLASH)) {
            prefix += SLASH_CHAR;
        }

        // ����pathβ����"/"
        boolean endsWithSlash = path.endsWith(SLASH);

        // ѹ��·���е�"."��".."
        StringTokenizer tokenizer = new StringTokenizer(path, "/");
        StringBuffer    buffer = new StringBuffer(prefix.length() + path.length());
        int             level  = 0;

        buffer.append(prefix);

        while (tokenizer.hasMoreTokens()) {
            String element = tokenizer.nextToken();

            // ����"."
            if (CURRENT_DIR.equals(element)) {
                continue;
            }

            // ��˷".."
            if (UP_LEVEL_DIR.equals(element)) {
                if (level == 0) {
                    // ���prefix���ڣ�������ͼԽ�����ϲ�Ŀ¼�����ǲ����ܵģ�
                    // ����null����ʾ·���Ƿ���
                    if (prefix.length() > 0) {
                        return null;
                    }

                    buffer.append(UP_LEVEL_DIR).append(SLASH_CHAR);
                } else {
                    level--;

                    boolean found = false;

                    for (int i = buffer.length() - 2; i >= prefix.length(); i--) {
                        if (buffer.charAt(i) == SLASH_CHAR) {
                            buffer.setLength(i + 1);
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        buffer.setLength(prefix.length());
                    }
                }

                continue;
            }

            // ��ӵ�path
            buffer.append(element).append(SLASH_CHAR);
            level++;
        }

        // ����ǿյ�·����������Ϊ"./"
        if (buffer.length() == 0) {
            buffer.append(CURRENT_DIR).append(SLASH_CHAR);
        }

        // ��������"/"
        if (!endsWithSlash && (buffer.length() > prefix.length())
                    && (buffer.charAt(buffer.length() - 1) == SLASH_CHAR)) {
            buffer.setLength(buffer.length() - 1);
        }

        return buffer.toString();
    }

    /**
     * ȡ�ú�ϵͳ��ص��ļ���ǰ׺������Windowsϵͳ������������������UNC·��ǰ׺"//hostname"�����������ǰ׺���򷵻ؿ��ַ�����
     *
     * @param path ����·��
     * @param isWindows �Ƿ�Ϊwindowsϵͳ
     *
     * @return ��ϵͳ��ص��ļ���ǰ׺�����·���Ƿ������磺"//"���򷵻�<code>null</code>
     */
    private static String getSystemDependentPrefix(String path, boolean isWindows) {
        if (isWindows) {
            // �ж�UNC·��
            if (path.startsWith(UNC_PREFIX)) {
                // �Ƿ�UNC·����"//"
                if (path.length() == UNC_PREFIX.length()) {
                    return null;
                }

                // ����·��Ϊ//hostname/subpath������//hostname
                int index = path.indexOf(SLASH, UNC_PREFIX.length());

                if (index != -1) {
                    return path.substring(0, index);
                } else {
                    return path;
                }
            }

            // �ж�Windows����·����"c:/..."
            if ((path.length() > 1) && (path.charAt(1) == COLON_CHAR)) {
                return path.substring(0, 2).toUpperCase();
            }
        }

        return "";
    }

    /* ============================================================================ */
    /*  ȡ�û���ָ��basedir���·����                                             */
    /* ============================================================================ */

    /**
     * ���ָ��·���Ѿ��Ǿ���·�������񻯺�ֱ�ӷ���֮������ȡ�û���ָ��basedir�Ĺ��·����
     * 
     * <p>
     * �÷����Զ��ж�����ϵͳ�����ͣ������windowsϵͳ����֧��UNC·��������������
     * </p>
     *
     * @param basedir ��Ŀ¼�����<code>path</code>Ϊ���·������ʾ���ڴ�Ŀ¼
     * @param path Ҫ����·��
     *
     * @return ��񻯵�·�������<code>path</code>�Ƿ�����<code>basedir</code>Ϊ<code>null</code>���򷵻�<code>null</code>
     */
    public static String getPathBasedOn(String basedir, String path) {
        return getPathBasedOn(basedir, path, SystemUtil.getOsInfo().isWindows());
    }

    /**
     * ���ָ��·���Ѿ��Ǿ���·�������񻯺�ֱ�ӷ���֮������ȡ�û���ָ��basedir�Ĺ��·����
     *
     * @param basedir ��Ŀ¼�����<code>path</code>Ϊ���·������ʾ���ڴ�Ŀ¼
     * @param path Ҫ����·��
     *
     * @return ��񻯵�·�������<code>path</code>�Ƿ�����<code>basedir</code>Ϊ<code>null</code>���򷵻�<code>null</code>
     */
    public static String getWindowsPathBasedOn(String basedir, String path) {
        return getPathBasedOn(basedir, path, true);
    }

    /**
     * ���ָ��·���Ѿ��Ǿ���·�������񻯺�ֱ�ӷ���֮������ȡ�û���ָ��basedir�Ĺ��·����
     *
     * @param basedir ��Ŀ¼�����<code>path</code>Ϊ���·������ʾ���ڴ�Ŀ¼
     * @param path Ҫ����·��
     *
     * @return ��񻯵�·�������<code>path</code>�Ƿ�����<code>basedir</code>Ϊ<code>null</code>���򷵻�<code>null</code>
     */
    public static String getUnixPathBasedOn(String basedir, String path) {
        return getPathBasedOn(basedir, path, false);
    }

    /**
     * ���ָ��·���Ѿ��Ǿ���·�������񻯺�ֱ�ӷ���֮������ȡ�û���ָ��basedir�Ĺ��·����
     *
     * @param basedir ��Ŀ¼�����<code>path</code>Ϊ���·������ʾ���ڴ�Ŀ¼
     * @param path Ҫ����·��
     * @param isWindows �Ƿ���windows·�������Ϊ<code>true</code>����֧������������UNC·��
     *
     * @return ��񻯵�·�������<code>path</code>�Ƿ�����<code>basedir</code>Ϊ<code>null</code>���򷵻�<code>null</code>
     */
    private static String getPathBasedOn(String basedir, String path, boolean isWindows) {
        /* ------------------------------------------- *
         * ����ȡ��path��ǰ׺���ж��Ƿ�Ϊ����·����    *
         * ����Ѿ��Ǿ���·���������normalize�󷵻ء� *
         * ------------------------------------------- */
        if (path == null) {
            return null;
        }

        path     = path.trim();

        // ��"\\"ת����"/"���Ա�ͳһ����
        path = path.replace(BACKSLASH_CHAR, SLASH_CHAR);

        // ȡ��ϵͳ�ض���·��ǰ׺������windowsϵͳ�������ǣ�"C:"����"//hostname"
        String prefix = getSystemDependentPrefix(path, isWindows);

        if (prefix == null) {
            return null;
        }

        // ����Ǿ���·������ֱ�ӷ���
        if ((prefix.length() > 0)
                    || ((path.length() > prefix.length())
                    && (path.charAt(prefix.length()) == SLASH_CHAR))) {
            return normalizePath(path, isWindows);
        }

        /* ------------------------------------------- *
         * �����Ѿ�ȷ��path�����·���ˣ��������Ҫ    *
         * ������basedir�ϲ���                         *
         * ------------------------------------------- */
        if (basedir == null) {
            return null;
        }

        StringBuffer buffer = new StringBuffer();

        buffer.append(basedir.trim());

        // ��ֹ�ظ���"/"���������׺�UNC prefix����
        if ((basedir.length() > 0) && (path.length() > 0)
                    && (basedir.charAt(basedir.length() - 1) != SLASH_CHAR)) {
            buffer.append(SLASH_CHAR);
        }

        buffer.append(path);

        return normalizePath(buffer.toString(), isWindows);
    }

    /* ============================================================================ */
    /*  ȡ�������ָ��basedir���·����                                             */
    /* ============================================================================ */

    /**
     * ȡ�������ָ����Ŀ¼�����·����
     * 
     * <p>
     * �÷����Զ��ж�����ϵͳ�����ͣ������windowsϵͳ����֧��UNC·��������������
     * </p>
     *
     * @param basedir ��Ŀ¼
     * @param path Ҫ�����·��
     *
     * @return ���<code>path</code>��<code>basedir</code>�Ǽ��ݵģ��򷵻������<code>basedir</code>�����·�������򷵻�<code>path</code>�������<code>basedir</code>���Ǿ���·��������·���Ƿ����򷵻�<code>null</code>
     */
    public static String getRelativePath(String basedir, String path) {
        return getRelativePath(basedir, path, SystemUtil.getOsInfo().isWindows());
    }

    /**
     * ȡ�������ָ����Ŀ¼�����·����
     *
     * @param basedir ��Ŀ¼
     * @param path Ҫ�����·��
     *
     * @return ���<code>path</code>��<code>basedir</code>�Ǽ��ݵģ��򷵻������<code>basedir</code>�����·�������򷵻�<code>path</code>�������<code>basedir</code>���Ǿ���·��������·���Ƿ����򷵻�<code>null</code>
     */
    public static String getWindowsRelativePath(String basedir, String path) {
        return getRelativePath(basedir, path, true);
    }

    /**
     * ȡ�������ָ����Ŀ¼�����·����
     *
     * @param basedir ��Ŀ¼
     * @param path Ҫ�����·��
     *
     * @return ���<code>path</code>��<code>basedir</code>�Ǽ��ݵģ��򷵻������<code>basedir</code>�����·�������򷵻�<code>path</code>�������<code>basedir</code>���Ǿ���·��������·���Ƿ����򷵻�<code>null</code>
     */
    public static String getUnixRelativePath(String basedir, String path) {
        return getRelativePath(basedir, path, false);
    }

    /**
     * ȡ�������ָ����Ŀ¼�����·����
     *
     * @param basedir ��Ŀ¼
     * @param path Ҫ�����·��
     * @param isWindows �Ƿ���windows·�������Ϊ<code>true</code>����֧������������UNC·��
     *
     * @return ���<code>path</code>��<code>basedir</code>�Ǽ��ݵģ��򷵻������<code>basedir</code>�����·�������򷵻�<code>path</code>�������<code>basedir</code>���Ǿ���·��������·���Ƿ����򷵻�<code>null</code>
     */
    private static String getRelativePath(String basedir, String path, boolean isWindows) {
        // ȡ�ù�񻯵�basedir��ȷ����Ϊ����·��
        basedir = normalizePath(basedir, isWindows);

        if (basedir == null) {
            return null;
        }

        String basePrefix = getSystemDependentPrefix(basedir, isWindows);

        if ((basePrefix == null) || ((basePrefix.length() == 0) && !basedir.startsWith(SLASH))) {
            return null; // basedir�����Ǿ���·��
        }

        // ȡ�ù�񻯵�path
        path = getPathBasedOn(basedir, path, isWindows);

        if (path == null) {
            return null;
        }

        String prefix = getSystemDependentPrefix(path, isWindows);

        // ���path��basedir��ǰ׺��ͬ������ת���������basedir�����·����
        // ֱ�ӷ��ع�񻯵�path���ɡ�
        if (!basePrefix.equals(prefix)) {
            return path;
        }

        // ����pathβ����"/"
        boolean endsWithSlash = path.endsWith(SLASH);

        // ��"/"�ָ�basedir��path
        String[]     baseParts = StringUtil.split(basedir.substring(basePrefix.length()), SLASH_CHAR);
        String[]     parts  = StringUtil.split(path.substring(prefix.length()), SLASH_CHAR);
        StringBuffer buffer = new StringBuffer();
        int          i      = 0;

        if (isWindows) {
            while ((i < baseParts.length) && (i < parts.length)
                        && baseParts[i].equalsIgnoreCase(parts[i])) {
                i++;
            }
        } else {
            while ((i < baseParts.length) && (i < parts.length) && baseParts[i].equals(parts[i])) {
                i++;
            }
        }

        if ((i < baseParts.length) && (i < parts.length)) {
            for (int j = i; j < baseParts.length; j++) {
                buffer.append(UP_LEVEL_DIR).append(SLASH_CHAR);
            }
        }

        for (; i < parts.length; i++) {
            buffer.append(parts[i]);

            if (i < (parts.length - 1)) {
                buffer.append(SLASH_CHAR);
            }
        }

        if (buffer.length() == 0) {
            buffer.append(CURRENT_DIR);
        }

        String relpath = buffer.toString();

        if (endsWithSlash && !relpath.endsWith(SLASH)) {
            relpath += SLASH;
        }

        return relpath;
    }

    /**
     * ��URL��ȡ���ļ���
     *
     * @param url URL
     *
     * @return �ļ�, ���URLΪ�գ��򲻴���һ���ļ�, �򷵻�<code>null</code>
     */
    public static File toFile(URL url) {
        if (url == null) {
            return null;
        }

        if (url.getProtocol().equals("file")) {
            String path = url.getPath();

            if (path != null) {
                return new File(StringEscapeUtil.unescapeURL(path));
            }
        }

        return null;
    }

    /**
     * ȡ��ָ��·�������ƺͺ�׺�� ����ֵΪ����Ϊ2�����飺
     * 
     * <ol>
     * <li>
     * ��һ��Ԫ��Ϊ��������׺��·�����ܲ�Ϊ<code>null</code>��
     * </li>
     * <li>
     * �ڶ���Ԫ��Ϊ��׺�������׺�����ڣ���Ϊ<code>null</code>��
     * </li>
     * </ol>
     * 
     *
     * @param path ·��
     *
     * @return ·���ͺ�׺������
     */
    public static String[] parseExtension(String path) {
        path = StringUtil.trimToEmpty(path);

        String[] parts = { path, null };

        if (StringUtil.isEmpty(path)) {
            return parts;
        }

        // ����ҵ���׺����index >= 0����extension != null������name��.��β��
        int    index     = StringUtil.lastIndexOf(path, EXTENSION_SEPARATOR);
        String extension = null;

        if (index >= 0) {
            extension = StringUtil.trimToNull(StringUtil.substring(path, index + 1));

            if (!StringUtil.containsNone(extension, ALL_SLASH)) {
                extension     = null;
                index         = -1;
            }
        }

        if (index >= 0) {
            parts[0] = StringUtil.substring(path, 0, index);
        }

        parts[1] = extension;

        return parts;
    }
}
